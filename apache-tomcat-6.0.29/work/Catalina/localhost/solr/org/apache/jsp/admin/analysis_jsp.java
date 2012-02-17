package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Payload;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.AttributeReflector;
import org.apache.solr.analysis.CharFilterFactory;
import org.apache.solr.analysis.TokenFilterFactory;
import org.apache.solr.analysis.TokenizerChain;
import org.apache.solr.analysis.TokenizerFactory;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.common.util.XML;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.math.BigInteger;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import java.io.File;
import java.net.InetAddress;
import java.io.StringWriter;
import org.apache.solr.core.Config;
import org.apache.solr.common.util.XML;
import org.apache.solr.common.SolrException;
import org.apache.lucene.LucenePackage;
import java.net.UnknownHostException;

public final class analysis_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


  // only try to figure out the hostname once in a static block so 
  // we don't have a potentially slow DNS lookup on every admin request
  static InetAddress addr = null;
  static String hostname = "unknown";
  static {
    try {
      addr = InetAddress.getLocalHost();
      hostname = addr.getCanonicalHostName();
    } catch (UnknownHostException e) {
      //default to unknown
    }
  }


  private static void doAnalyzer(JspWriter out, SchemaField field, String val, boolean queryAnalyser, boolean verbose, Set<String> match) throws Exception {

    FieldType ft = field.getType();
     Analyzer analyzer = queryAnalyser ?
             ft.getQueryAnalyzer() : ft.getAnalyzer();
     if (analyzer instanceof TokenizerChain) {
       TokenizerChain tchain = (TokenizerChain)analyzer;
       CharFilterFactory[] cfiltfacs = tchain.getCharFilterFactories();
       TokenizerFactory tfac = tchain.getTokenizerFactory();
       TokenFilterFactory[] filtfacs = tchain.getTokenFilterFactories();

       if( cfiltfacs != null ){
         String source = val;
         for(CharFilterFactory cfiltfac : cfiltfacs ){
           CharStream reader = CharReader.get(new StringReader(source));
           reader = cfiltfac.create(reader);
           if(verbose){
             writeHeader(out, cfiltfac.getClass(), cfiltfac.getArgs());
             source = writeCharStream(out, reader);
           }
         }
       }

       TokenStream tstream = tfac.create(tchain.charStream(new StringReader(val)));
       List<AttributeSource> tokens = getTokens(tstream);
       if (verbose) {
         writeHeader(out, tfac.getClass(), tfac.getArgs());
       }

       writeTokens(out, tokens, ft, verbose, match);

       for (TokenFilterFactory filtfac : filtfacs) {
         if (verbose) {
           writeHeader(out, filtfac.getClass(), filtfac.getArgs());
         }

         final Iterator<AttributeSource> iter = tokens.iterator();
         tstream = filtfac.create( new TokenStream(tstream.getAttributeFactory()) {
           
           public boolean incrementToken() throws IOException {
             if (iter.hasNext()) {
               clearAttributes();
               AttributeSource token = iter.next();
               Iterator<Class<? extends Attribute>> atts = token.getAttributeClassesIterator();
               while (atts.hasNext()) // make sure all att impls in the token exist here
                 addAttribute(atts.next());
               token.copyTo(this);
               return true;
             } else {
               return false;
             }
           }
          }
         );
         tokens = getTokens(tstream);

         writeTokens(out, tokens, ft, verbose, match);
       }

     } else {
       TokenStream tstream = analyzer.reusableTokenStream(field.getName(),new StringReader(val));
       tstream.reset();
       List<AttributeSource> tokens = getTokens(tstream);
       if (verbose) {
         writeHeader(out, analyzer.getClass(), Collections.EMPTY_MAP);
       }
       writeTokens(out, tokens, ft, verbose, match);
     }
  }


  static List<AttributeSource> getTokens(TokenStream tstream) throws IOException {
    List<AttributeSource> tokens = new ArrayList<AttributeSource>();
    tstream.reset();
    while (tstream.incrementToken()) {
      tokens.add(tstream.cloneAttributes());
    }
    return tokens;
  }

  private static class ReflectItem {
    final Class<? extends Attribute> attClass;
    final String key;
    final Object value;
    
    ReflectItem(Class<? extends Attribute> attClass, String key, Object value) {
      this.attClass = attClass;
      this.key = key;
      this.value = value;
    }
  }
  
  private static class Tok {
    final String term;
    final int pos;
    final List<ReflectItem> reflected = new ArrayList<ReflectItem>();
    
    Tok(AttributeSource token, int pos) {
      this.term = token.addAttribute(CharTermAttribute.class).toString();
      this.pos = pos;
      token.reflectWith(new AttributeReflector() {
        public void reflect(Class<? extends Attribute> attClass, String key, Object value) {
          // leave out position and term
          if (CharTermAttribute.class.isAssignableFrom(attClass))
            return;
          if (PositionIncrementAttribute.class.isAssignableFrom(attClass))
            return;
          reflected.add(new ReflectItem(attClass, key, value));
        }
      });
    }
  }

  private static interface TokToStr {
    public String toStr(Tok o);
  }

  private static void printRow(JspWriter out, String header, String headerTitle, List<Tok>[] arrLst, TokToStr converter, boolean multival, boolean verbose, Set<String> match) throws IOException {
    // find the maximum number of terms for any position
    int maxSz=1;
    if (multival) {
      for (List lst : arrLst) {
        maxSz = Math.max(lst.size(), maxSz);
      }
    }


    for (int idx=0; idx<maxSz; idx++) {
      out.println("<tr>");
      if (idx==0 && verbose) {
        if (header != null) {
          out.print("<th NOWRAP rowspan=\""+maxSz+"\"");
          if (headerTitle != null) {
            out.print(" title=\"");
            XML.escapeCharData(headerTitle,out);
            out.print("\"");
          }
          out.print(">");
          XML.escapeCharData(header,out);
          out.println("</th>");
        }
      }

      for (int posIndex=0; posIndex<arrLst.length; posIndex++) {
        List<Tok> lst = arrLst[posIndex];
        if (lst.size() <= idx) continue;
        if (match!=null && match.contains(lst.get(idx).term)) {
          out.print("<td class=\"highlight\"");
        } else {
          out.print("<td class=\"debugdata\"");
        }

        // if the last value in the column, use up
        // the rest of the space via rowspan.
        if (lst.size() == idx+1 && lst.size() < maxSz) {
          out.print("rowspan=\""+(maxSz-lst.size()+1)+'"');
        }

        out.print('>');

        XML.escapeCharData(converter.toStr(lst.get(idx)), out);
        out.print("</td>");
      }

      out.println("</tr>");
    }

  }

  /* this method is totally broken, as no charset involved: new String(byte[]) is crap!
  static String isPayloadString( Payload p ) {
    String sp = new String(p.getData());
    for( int i=0; i < sp.length(); i++ ) {
      if( !Character.isDefined( sp.charAt(i) ) || Character.isISOControl( sp.charAt(i) ) )
        return "";
      }
    return "(" + sp + ")";
  }
  */

  static void writeHeader(JspWriter out, Class clazz, Map<String,String> args) throws IOException {
    out.print("<h4>");
    out.print(clazz.getName());
    XML.escapeCharData("   "+args,out);
    out.println("</h4>");
  }



  // readable, raw, pos, type, start/end
  static void writeTokens(JspWriter out, List<AttributeSource> tokens, final FieldType ft, boolean verbose, Set<String> match) throws IOException {

    // Use a map to tell what tokens are in what positions
    // because some tokenizers/filters may do funky stuff with
    // very large increments, or negative increments.
    HashMap<Integer,List<Tok>> map = new HashMap<Integer,List<Tok>>();
    boolean needRaw=false;
    int pos=0, reflectionCount = -1;
    for (AttributeSource t : tokens) {
      String text = t.addAttribute(CharTermAttribute.class).toString();
      if (!text.equals(ft.indexedToReadable(text))) {
        needRaw=true;
      }

      pos += t.addAttribute(PositionIncrementAttribute.class).getPositionIncrement();
      List lst = map.get(pos);
      if (lst==null) {
        lst = new ArrayList(1);
        map.put(pos,lst);
      }
      Tok tok = new Tok(t,pos);
      // sanity check
      if (reflectionCount < 0) {
        reflectionCount = tok.reflected.size();
      } else {
        if (reflectionCount != tok.reflected.size())
          throw new RuntimeException("Should not happen: Number of reflected entries differs for position=" + pos);
      }
      lst.add(tok);
    }

    List<Tok>[] arr = (List<Tok>[])map.values().toArray(new ArrayList[map.size()]);

    // Jetty 6.1.3 miscompiles a generics-enabled version..., without generics:
    Arrays.sort(arr, new Comparator() {
      public int compare(Object toks, Object toks1) {
        return ((List<Tok>)toks).get(0).pos - ((List<Tok>)toks1).get(0).pos;
      }
    });

    out.println("<table width=\"auto\" class=\"analysis\" border=\"1\">");

    if (verbose) {
      printRow(out, "position", "calculated from " + PositionIncrementAttribute.class.getName(), arr, new TokToStr() {
        public String toStr(Tok t) {
          return Integer.toString(t.pos);
        }
      },false,verbose,null);
    }

    printRow(out, "term text", CharTermAttribute.class.getName(), arr, new TokToStr() {
      public String toStr(Tok t) {
        return ft.indexedToReadable(t.term);
      }
    },true,verbose,match);

    if (verbose) {
      if (needRaw) {
        printRow(out, "raw text", CharTermAttribute.class.getName(), arr, new TokToStr() {
        public String toStr(Tok t) {
            // page is UTF-8, so anything goes.
            return t.term;
          }
        },true,verbose,match);
      }

      for (int att=0; att < reflectionCount; att++) {
        final ReflectItem item0 = arr[0].get(0).reflected.get(att);
        final int i = att;
        printRow(out, item0.key, item0.attClass.getName(), arr, new TokToStr() {
          public String toStr(Tok t) {
            final ReflectItem item = t.reflected.get(i);
            if (item0.attClass != item.attClass || !item0.key.equals(item.key))
              throw new RuntimeException("Should not happen: attribute types suddenly change at position=" + t.pos);
            if (item.value instanceof Payload) {
              Payload p = (Payload) item.value;
              if( null != p ) {
                BigInteger bi = new BigInteger( p.getData() );
                String ret = bi.toString( 16 );
                if (ret.length() % 2 != 0) {
                  // Pad with 0
                  ret = "0"+ret;
                }
                //TODO maybe fix: ret += isPayloadString(p);
                return ret;
              }
              return "";
            } else {
              return (item.value != null) ? item.value.toString() : "";
            }
          }
        },true,verbose,null);
      }
    }
    
    out.println("</table>");
  }

  static String writeCharStream(JspWriter out, CharStream input) throws IOException {
    out.println("<table width=\"auto\" class=\"analysis\" border=\"1\">");
    out.println("<tr>");

    out.print("<th NOWRAP>");
    XML.escapeCharData("text",out);
    out.println("</th>");

    final int BUFFER_SIZE = 1024;
    char[] buf = new char[BUFFER_SIZE];
    int len = 0;
    StringBuilder sb = new StringBuilder();
    do {
      len = input.read( buf, 0, BUFFER_SIZE );
      if( len > 0 )
        sb.append(buf, 0, len);
    } while( len == BUFFER_SIZE );
    out.print("<td class=\"debugdata\">");
    XML.escapeCharData(sb.toString(),out);
    out.println("</td>");
    
    out.println("</tr>");
    out.println("</table>");
    return sb.toString();
  }


  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/admin/header.jsp");
    _jspx_dependants.add("/admin/_info.jsp");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("<html>\n");
      out.write("<head>\n");

request.setCharacterEncoding("UTF-8");

      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');

  // 
  SolrCore  core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");
  if (core == null) {
    response.sendError( 404, "missing core name in path" );
    return;
  }
    
  SolrConfig solrConfig = core.getSolrConfig();
  int port = request.getServerPort();
  IndexSchema schema = core.getSchema();

  // enabled/disabled is purely from the point of a load-balancer
  // and has no effect on local server function.  If there is no healthcheck
  // configured, don't put any status on the admin pages.
  String enabledStatus = null;
  String enabledFile = solrConfig.get("admin/healthcheck/text()",null);
  boolean isEnabled = false;
  if (enabledFile!=null) {
    isEnabled = new File(enabledFile).exists();
  }

  String collectionName = schema!=null ? schema.getName():"unknown";

  String defaultSearch = "";
  { 
    StringWriter tmp = new StringWriter();
    XML.escapeCharData
      (solrConfig.get("admin/defaultQuery/text()", ""), tmp);
    defaultSearch = tmp.toString();
  }

  String solrImplVersion = "";
  String solrSpecVersion = "";
  String luceneImplVersion = "";
  String luceneSpecVersion = "";

  { 
    Package p;
    StringWriter tmp;

    p = SolrCore.class.getPackage();

    tmp = new StringWriter();
    solrImplVersion = p.getImplementationVersion();
    if (null != solrImplVersion) {
      XML.escapeCharData(solrImplVersion, tmp);
      solrImplVersion = tmp.toString();
    }
    tmp = new StringWriter();
    solrSpecVersion = p.getSpecificationVersion() ;
    if (null != solrSpecVersion) {
      XML.escapeCharData(solrSpecVersion, tmp);
      solrSpecVersion = tmp.toString();
    }
  
    p = LucenePackage.class.getPackage();

    tmp = new StringWriter();
    luceneImplVersion = p.getImplementationVersion();
    if (null != luceneImplVersion) {
      XML.escapeCharData(luceneImplVersion, tmp);
      luceneImplVersion = tmp.toString();
    }
    tmp = new StringWriter();
    luceneSpecVersion = p.getSpecificationVersion() ;
    if (null != luceneSpecVersion) {
      XML.escapeCharData(luceneSpecVersion, tmp);
      luceneSpecVersion = tmp.toString();
    }
  }
  
  String cwd=System.getProperty("user.dir");
  String solrHome= solrConfig.getInstanceDir();
  
  boolean cachingEnabled = !solrConfig.getHttpCachingConfig().isNever304(); 

      out.write('\n');
      out.write("\n");
      out.write("<script>\n");
      out.write("var host_name=\"");
      out.print( hostname );
      out.write("\"\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"solr-admin.css\">\n");
      out.write("<link rel=\"icon\" href=\"favicon.ico\" type=\"image/ico\"></link>\n");
      out.write("<link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/ico\"></link>\n");
      out.write("<title>Solr admin page</title>\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("<a href=\".\"><img border=\"0\" align=\"right\" height=\"78\" width=\"142\" src=\"solr_small.png\" alt=\"Solr\"></a>\n");
      out.write("<h1>Solr Admin (");
      out.print( collectionName );
      out.write(')');
      out.write('\n');
      out.print( enabledStatus==null ? "" : (isEnabled ? " - Enabled" : " - Disabled") );
      out.write(" </h1>\n");
      out.write("\n");
      out.print( hostname );
      out.write(':');
      out.print( port );
      out.write("<br/>\n");
      out.write("cwd=");
      out.print( cwd );
      out.write("  SolrHome=");
      out.print( solrHome );
      out.write("\n");
      out.write("<br/>\n");
String cachingStatus = " HTTP caching is ";  
      out.write('\n');
      out.print( cachingEnabled ? cachingStatus + " ON": cachingStatus + " OFF" );
      out.write('\n');
      out.write('\n');
      out.write('\n');

  // is name a field name or a type name?
  String nt = request.getParameter("nt");
  if (nt==null || nt.length()==0) nt="name"; // assume field name
  nt = nt.toLowerCase(Locale.ENGLISH).trim();
  String name = request.getParameter("name");
  if (name==null || name.length()==0) name="";
  String val = request.getParameter("val");
  if (val==null || val.length()==0) val="";
  String qval = request.getParameter("qval");
  if (qval==null || qval.length()==0) qval="";
  String verboseS = request.getParameter("verbose");
  boolean verbose = verboseS!=null && verboseS.equalsIgnoreCase("on");
  String qverboseS = request.getParameter("qverbose");
  boolean qverbose = qverboseS!=null && qverboseS.equalsIgnoreCase("on");
  String highlightS = request.getParameter("highlight");
  boolean highlight = highlightS!=null && highlightS.equalsIgnoreCase("on");

      out.write("\n");
      out.write("\n");
      out.write("<br clear=\"all\">\n");
      out.write("\n");
      out.write("<h2>Field Analysis</h2>\n");
      out.write("\n");
      out.write("<form method=\"POST\" action=\"analysis.jsp\" accept-charset=\"UTF-8\">\n");
      out.write("<table>\n");
      out.write("<tr>\n");
      out.write("  <td>\n");
      out.write("  <strong>Field\n");
      out.write("          <select name=\"nt\">\n");
      out.write("    <option ");
      out.print( nt.equals("name") ? "selected=\"selected\"" : "" );
      out.write(" >name</option>\n");
      out.write("    <option ");
      out.print( nt.equals("type") ? "selected=\"selected\"" : "" );
      out.write(">type</option>\n");
      out.write("          </select></strong>\n");
      out.write("  </td>\n");
      out.write("  <td>\n");
      out.write("  <input class=\"std\" name=\"name\" type=\"text\" value=\"");
 XML.escapeCharData(name, out); 
      out.write("\">\n");
      out.write("  </td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n");
      out.write("  <td>\n");
      out.write("  <strong>Field value (Index)</strong>\n");
      out.write("  <br/>\n");
      out.write("  verbose output\n");
      out.write("  <input name=\"verbose\" type=\"checkbox\"\n");
      out.write("     ");
      out.print( verbose ? "checked=\"true\"" : "" );
      out.write(" >\n");
      out.write("    <br/>\n");
      out.write("  highlight matches\n");
      out.write("  <input name=\"highlight\" type=\"checkbox\"\n");
      out.write("     ");
      out.print( highlight ? "checked=\"true\"" : "" );
      out.write(" >\n");
      out.write("  </td>\n");
      out.write("  <td>\n");
      out.write("  <textarea class=\"std\" rows=\"8\" cols=\"70\" name=\"val\">");
 XML.escapeCharData(val,out); 
      out.write("</textarea>\n");
      out.write("  </td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n");
      out.write("  <td>\n");
      out.write("  <strong>Field value (Query)</strong>\n");
      out.write("  <br/>\n");
      out.write("  verbose output\n");
      out.write("  <input name=\"qverbose\" type=\"checkbox\"\n");
      out.write("     ");
      out.print( qverbose ? "checked=\"true\"" : "" );
      out.write(" >\n");
      out.write("  </td>\n");
      out.write("  <td>\n");
      out.write("  <textarea class=\"std\" rows=\"1\" cols=\"70\" name=\"qval\">");
 XML.escapeCharData(qval,out); 
      out.write("</textarea>\n");
      out.write("  </td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n");
      out.write("\n");
      out.write("  <td>\n");
      out.write("  </td>\n");
      out.write("\n");
      out.write("  <td>\n");
      out.write("  <input class=\"stdbutton\" type=\"submit\" value=\"analyze\">\n");
      out.write("  </td>\n");
      out.write("\n");
      out.write("</tr>\n");
      out.write("</table>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("\n");

  SchemaField field=null;

  if (name!="") {
    if (nt.equals("name")) {
      try {
        field = schema.getField(name);
      } catch (Exception e) {
        out.print("<strong>Unknown Field: ");
        XML.escapeCharData(name, out);
        out.println("</strong>");
      }
    } else {
       FieldType t = schema.getFieldTypes().get(name);
       if (null == t) {
        out.print("<strong>Unknown Field Type: ");
        XML.escapeCharData(name, out);
        out.println("</strong>");
       } else {
         field = new SchemaField("fakefieldoftype:"+name, t);
       }
    }
  }

  if (field!=null) {
    HashSet<String> matches = null;
    if (qval!="" && highlight) {
      Reader reader = new StringReader(qval);
      Analyzer analyzer =  field.getType().getQueryAnalyzer();
      TokenStream tstream = analyzer.reusableTokenStream(field.getName(),reader);
      CharTermAttribute termAtt = tstream.addAttribute(CharTermAttribute.class);
      tstream.reset();
      matches = new HashSet<String>();
      while (tstream.incrementToken()) {
        matches.add(termAtt.toString());
      }
    }

    if (val!="") {
      out.println("<h3>Index Analyzer</h3>");
      doAnalyzer(out, field, val, false, verbose, matches);
    }
    if (qval!="") {
      out.println("<h3>Query Analyzer</h3>");
      doAnalyzer(out, field, qval, true, qverbose, null);
    }
  }


      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
