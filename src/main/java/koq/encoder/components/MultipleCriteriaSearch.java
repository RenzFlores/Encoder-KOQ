package koq.encoder.components;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import koq.encoder.classes.Grade;
import koq.encoder.classes.GradePeriod;
import koq.encoder.classes.Row;

public class MultipleCriteriaSearch {

    private JTable table;
    private GradePeriod model;

    public MultipleCriteriaSearch(GradePeriod model) {
        this.model = model;
    }

    // Search Criteria Class to store each search condition
    public static class SearchCriteria {
        String searchTerm;
        boolean isNumeric; // Whether the search term is a number (for comparisons)

        public SearchCriteria(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }

    // Method to perform multiple criteria search
    public List<Integer> advancedSearchCustomTable(List<SearchCriteria> criteriaList) {
        List<Row> matchingRows = new ArrayList<>();
        
        double min;
        double max;
            
        if (criteriaList.get(1) != null) {
            min = Double.parseDouble(criteriaList.get(1).searchTerm);
        } else {
            min = 0.0;
        }

        if (criteriaList.get(2) != null) {
            max = Double.parseDouble(criteriaList.get(2).searchTerm);
        } else {
            max = 100.0;
        }
        
        System.out.println(min);
        System.out.println(max);
        
        // Copy row data
        for (Row r : model.getRows()) {
            matchingRows.add(r);
        }

        // Filter rows by student name
        if (criteriaList.get(0) != null) {
            // Iterate through the rows of the table model
            Iterator<Row> it = matchingRows.iterator();
            while (it.hasNext()) {
                Row r = it.next();
                // Remove rows that are not matching. Case insensitive 
                if (! r.getStudent().getStudentFullName().toLowerCase().contains(criteriaList.get(0).searchTerm)) {
                   it.remove();
                }
            }
        }
        
        Iterator<Row> itRow = matchingRows.iterator();
        while (itRow.hasNext()) {
            Row r = itRow.next();
            
            List<Double> gradeList = new ArrayList<>();
            
            if (Integer.parseInt(criteriaList.get(3).searchTerm) != -1) {
                for (Grade g : r.getGrades()) {
                    // Add only specific grades
                    if (g.getActivityTypeId() == Integer.parseInt(criteriaList.get(3).searchTerm)) {
                        gradeList.add((double)g.getGrade() / (double)g.getTotalScore());
                    }
                }
            } else {
                // Add all
                for (Grade g : r.getGrades()) {
                    gradeList.add((double)g.getGrade() / (double)g.getTotalScore());
                }
            }
            
            // Remove grades outside of search range
            Iterator<Double> itDouble = gradeList.iterator();
            while (itDouble.hasNext()) {
                Double score = itDouble.next();
                if (score < min || score > max) {
                    itDouble.remove();
                }
            }
            
            // Remove row if no grades are matched
            if (gradeList.isEmpty()) {
                itRow.remove();
            }
        }

        List<Integer> indices = new ArrayList<>();
        
        for (Row r : matchingRows) {
            indices.add(model.getRows().indexOf(r));
        }
        
        return indices;
    }
}