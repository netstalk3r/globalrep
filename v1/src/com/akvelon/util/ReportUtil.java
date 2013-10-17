package com.akvelon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akvelon.report.Report;

public class ReportUtil {

	/**
	 * Sort reports by bli owner
	 * 
	 * @param reports - list where each element is list with reports that belong
	 *            to one BLI
	 * @return list where each element is list with reports that belong to one
	 *         owner
	 */
	public static List<List<Report>> sortReportsByBliOwner(List<List<Report>> reports) {
		Map<String, List<Report>> res = new HashMap<String, List<Report>>();
		for (List<Report> reps : reports) {
			for (Report rep : reps) {
				if (!res.keySet().contains(rep.getBliOwner())) {
					List<Report> listRep = new ArrayList<Report>();
					listRep.add(rep);
					res.put(rep.getBliOwner(), listRep);
				} else {
					res.get(rep.getBliOwner()).add(rep);
				}
			}
		}
		return new ArrayList<List<Report>>(res.values());
	}
}
