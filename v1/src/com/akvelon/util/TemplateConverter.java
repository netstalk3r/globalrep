package com.akvelon.util;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.akvelon.report.Report;

public class TemplateConverter {

	private static final String TEMPLATE = "src/report_tepmlate.vm";

	private VelocityEngine velocityEng;
	private Template template;
	private VelocityContext context;

	public TemplateConverter() {
		velocityEng = new VelocityEngine();
		velocityEng.init();
		template = velocityEng.getTemplate(TEMPLATE);
		context = new VelocityContext();
	}

	public String convertToHTML(List<List<Report>> reports) {
		
		context.put("reports", reports);
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
}
