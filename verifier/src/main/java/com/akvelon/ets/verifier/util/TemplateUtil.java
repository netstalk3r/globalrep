package com.akvelon.ets.verifier.util;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.DateTool;

import com.akvelon.ets.verifier.reports.PersonalHourReport;

public class TemplateUtil {
	
	private static final String ALL_HOURS_REPORTS = "all_perorts_template.vm";
	private static final String MISSED_HOURS_REPORTS = "missed_hours_template.vm";
	
	private static VelocityEngine velocityEng;
	private static Template template;
	private static VelocityContext context;
	
	static {
		velocityEng = new VelocityEngine();
		velocityEng.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEng.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEng.init();
	}
	
	public static String getTemplateForAllReportedHours(List<PersonalHourReport> reports, int requiredHours) {
		Map<String,Object> contextParams = new HashMap<String, Object>(2);
		contextParams.put("reps", reports);
		contextParams.put("reqHours", requiredHours);
		contextParams.put("curDate", new Date());
		return getTemplate(ALL_HOURS_REPORTS,contextParams);
	}

	public static String getTemplateForMissedHours(PersonalHourReport report, int requiredHours) {
		Map<String,Object> contextParams = new HashMap<String, Object>(2);
		contextParams.put("rep", report);
		contextParams.put("reqHours", requiredHours);
		contextParams.put("curDate", new Date());
		return getTemplate(MISSED_HOURS_REPORTS,contextParams);
	}
	
	private static String getTemplate(String templ, Map<String,Object> contextParams) {
		template = velocityEng.getTemplate(templ);
		context = new VelocityContext();
		
 		for (Entry<String,Object> contextParm : contextParams.entrySet()) {
 			context.put(contextParm.getKey(), contextParm.getValue());
 		}
 		context.put("dateTool", new DateTool());
 		
 		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
}
