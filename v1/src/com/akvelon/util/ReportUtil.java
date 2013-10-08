package com.akvelon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akvelon.report.Report;

public class ReportUtil {

	public static List<List<Report>> sortReportsByOwner(List<List<Report>> reports) {
		Map<String, List<Report>> res = new HashMap<String, List<Report>>();
		for (List<Report> reps : reports) {
			for (Report rep : reps) {
				if (!res.keySet().contains(rep.getOwnerTaskName())) {
					List<Report> listRep = new ArrayList<Report>();
					listRep.add(rep);
					res.put(rep.getOwnerTaskName(), listRep);
				} else {
					res.get(rep.getOwnerTaskName()).add(rep);
				}
			}
		}
		return new ArrayList<List<Report>>(res.values());
	}
}
