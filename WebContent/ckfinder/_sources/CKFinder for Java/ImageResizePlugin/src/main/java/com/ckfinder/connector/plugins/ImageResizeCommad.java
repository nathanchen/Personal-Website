/*
 * CKFinder
 * ========
 * http://ckfinder.com
 * Copyright (C) 2007-2012, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.ckfinder.connector.plugins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.ckfinder.connector.data.EventArgs;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.data.PluginInfo;
import com.ckfinder.connector.data.PluginParam;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.handlers.command.XMLCommand;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.ImageUtils;

public class ImageResizeCommad extends XMLCommand implements IEventHandler {


	private PluginInfo pluginInfo;
	/**
	 * file name
	 */
	private String fileName;
	private String newFileName;
	private String overwrite;
	private Integer width;
	private Integer height;
	private boolean wrongReqSizesParams;
	private Map<String, String> sizesFromReq;

	private static final String[] SIZES = {"small", "medium", "large"};

	public boolean runEventHandler(EventArgs eventArgs, IConfiguration configuration1)
			throws ConnectorException {
		BeforeExecuteCommandEventArgs args = (BeforeExecuteCommandEventArgs) eventArgs;
		if ("ImageResize".equals(args.getCommand())){
			this.runCommand(args.getRequest(), args.getResponse(), configuration1);
			return false;
		}
		return true;
	}


	@Override
	protected void createXMLChildNodes(int arg0, Element arg1)
			throws ConnectorException {

	}

	@Override
	protected int getDataForXml() {
		if (!AccessControlUtil.getInstance(configuration)
				.checkFolderACL(type, currentFolder, userRole,
					AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_DELETE
				| AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
		}

		if (this.fileName == null || this.fileName.equals("")) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
		}

		if (!FileUtils.checkFileName(fileName)
				||  FileUtils.checkIfFileIsHidden(fileName, configuration)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
		}


		if (FileUtils.checkFileExtension(fileName, configuration.getTypes().get(type),
										configuration, false) == 1) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
		}

		File file = new File(configuration.getTypes().get(type).getPath() + this.currentFolder,
								fileName);
		try {
			if (!(file.exists() && file.isFile())) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
			}

			if (this.wrongReqSizesParams) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}

			if (this.width != null && this.height != null) {

				if (!FileUtils.checkFileName(this.newFileName)
						&& FileUtils.checkIfFileIsHidden(this.newFileName, configuration)) {
					return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
				}

				if (FileUtils.checkFileExtension(this.newFileName,
						configuration.getTypes().get(this.type), configuration, false) == 1) {
					return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
				}

				File thumbFile =  new File(configuration.getTypes().get(type).getPath() + this.currentFolder,
																this.newFileName);

				if (thumbFile.canWrite()) {
					return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
				}
				if (!"1".equals(this.overwrite) && thumbFile.exists()) {
					return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
				}
				int maxImageHeight = configuration.getImgHeight();
				int maxImageWidth = configuration.getImgWidth();
				if ((maxImageWidth > 0 && this.width > maxImageWidth )
						|| ( maxImageHeight > 0 && this.height > maxImageHeight)) {
					return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
				}

				try {
					ImageUtils.createResizedImage(file, thumbFile,
							this.width, this.height, configuration.getImgQuality());

				} catch (IOException e) {
					if (configuration.isDebugMode()) {
						this.exception = e;
					}
					return Constants.Errors.
								CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
				}
			}

			String fileNameWithoutExt = FileUtils.getFileNameWithoutExtension(fileName);
			String fileExt = FileUtils.getFileExtension(fileName);
			for (String size : SIZES) {
				if (sizesFromReq.get(size) != null
						&& sizesFromReq.get(size).equals("1")) {
					String thumbName = fileNameWithoutExt
													.concat("_")
													.concat(size)
													.concat(".")
													.concat(fileExt);
					File thumbFile = new File(configuration.getTypes().get(this.type).getPath()
															.concat(this.currentFolder)
															.concat(thumbName));
					for (PluginParam param : pluginInfo.getParams()) {
						if (size.concat("Thumb").equals(param.getName())) {
							if (checkParamSize(param.getValue())) {
								String[] params = parseValue(param.getValue());
								try {
									ImageUtils.createResizedImage(file, thumbFile, Integer.valueOf(params[0]),
															Integer.valueOf(params[1]), configuration.getImgQuality());
								} catch (IOException e) {
									if (configuration.isDebugMode()) {
										this.exception = e;
									}
									return Constants.Errors.
													CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
								}
							}
						}
					}
				}
			}
		} catch (SecurityException e) {
			if (configuration.isDebugMode()) {
				this.exception = e;
			}
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
		}

		return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
	}


	private String[] parseValue(String value) {
		StringTokenizer st = new StringTokenizer(value, "x");
		String[] res = new String[2];
		res[0] = st.nextToken();
		res[1] = st.nextToken();
		return res;
	}


	private boolean checkParamSize(String value) {
		return Pattern.matches("(\\d)+x(\\d)+", value);
	}


	@Override
	public void initParams(HttpServletRequest request,
			IConfiguration configuration1, Object... params)
			throws ConnectorException {
		super.initParams(request, configuration1, params);

		this.sizesFromReq = new HashMap<String, String>();
		this.fileName = getParameter(request, "fileName");
		this.newFileName = getParameter(request, "newFileName");
		this.overwrite = request.getParameter("overwrite");
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		this.wrongReqSizesParams = false;
		try {
			if (width != null && !width.equals("")) {
				this.width = Integer.valueOf(width);
			} else {
				this.width = null;
			}
		} catch (NumberFormatException e) {
			this.width = null;
			this.wrongReqSizesParams = true;
		}
		try {
			if (height != null && !height.equals("")) {
				this.height = Integer.valueOf(height);
			} else {
				this.height = null;
			}
		} catch (NumberFormatException e) {
			this.height = null;
			this.wrongReqSizesParams = true;
		}
		for (String size : SIZES) {
			sizesFromReq.put(size, request.getParameter(size));
		}

	}

	public ImageResizeCommad(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}


}
