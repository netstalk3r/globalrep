package com.akvelon.util;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;

import com.akvelon.report.Report;

public class TemplateConverter {

	private static final Logger log = Logger.getLogger(TemplateConverter.class);
	
	private static final String REP_TEMPLATE = "src/report_tepmlate.vm";
	private static final String BLI_OWNER_TEMPLATE = "src/bli_owner_tepmlate.vm";

	private VelocityEngine velocityEng;
	private Template template;
	private VelocityContext context;

	public TemplateConverter() {
		velocityEng = new VelocityEngine();
		velocityEng.init();
	}

	public String convertToHTMLByRepType(List<List<Report>> reports) {
		return convert(REP_TEMPLATE,reports,ReportUtil.sortReportsByTaskOwner(reports));
	}
	
	public String convertToHTMLByTaskOwner(List<List<Report>> reports) {
		return convert(BLI_OWNER_TEMPLATE,ReportUtil.sortReportsByTaskOwner(reports),null);
	}
	
	private String convert(String templ, List<List<Report>> repsByType, List<List<Report>> repsByOwner) {
		template = velocityEng.getTemplate(templ);
		context = new VelocityContext();
		
		context.put("reports", repsByType);
		if (repsByOwner != null) {
			context.put("repsByOwner", repsByOwner);
		}
		context.put("date",  new DateTool());
		context.put("curDate", new Date());
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		log.debug(writer.toString());
		return writer.toString();
	}
}
