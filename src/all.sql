
create table Nathan_articles(
	article_id varchar(100) not null primary key,
	author_name varchar(100) not null,
	article_body varchar(100000),
	publish_date date not null,
	article_title varchar(100) not null
);


create table Nathan_comments(
	article_id varchar(100) not null,
	viewer_name varchar(100) not null default 'guest',
	message varchar(10000),
	publish_date date not null,
	comment_id varchar(100) not null,
	primary key (article_id, comment_id),
	CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES nathan_articles(article_id)
);

create table Users(
	user_name varchar(100) not null primary key,
	password varchar(20) not null
);

create table Users_role(
	user_name varchar(100) not null primary key,
	role varchar(10) not null default 'guest'
);


insert into Nathan_comments(article_id, message, publish_date, comment_id) values ('1', 'You are right !', '2009-06-14', '1');

insert into Nathan_comments values ('1', 'Mike', 'I knew that ...', '2009-06-15', '2');

insert into Nathan_comments values ('2', 'Tom', 'This post is useless ?', '2009-04-05', '1');

insert into Users values ('jeff', 'jeff');
insert into Users values ('bob', 'bob');

insert into Users_role values ('jeff', 'admin');
insert into Users_role(user_name) values ('bob');

insert into Nathan_articles values(
	'1',
	"bob",
	"The model has a central position in a Play! application. It is the domain-specific 
                    representation of the information on which the application operates.
                    
                    Martin fowler defines it as:
                        
                    Responsible for representing concepts of the business, information about the 
                    business situation, and business rules. State that reflects the business situation 
                    is controlled and used here, even though the technical details of storing it are 
                    delegated to the infrastructure. This layer is the heart of business software.",
    '2009-06-14',
    'About the model layer'
);


insert into nathan_articles values(
	"2",
	"bob",
	"Well, it's just a test.",
    "2009-03-25",
    "Just a test of YABE"
);

insert into Nathan_articles values(
	'3',
	"jeff",
	"A Play! application follows the MVC architectural pattern as applied to the 
                    architecture of the Web.
                    
                    This pattern splits the application into separate layers: the Presentation 
                    layer and the Model layer. The Presentation layer is further split into a 
                    View and a Controller layer.",
    '2009-03-25',
    'The MVC application'
);
