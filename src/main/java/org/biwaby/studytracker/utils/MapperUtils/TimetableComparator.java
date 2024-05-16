package org.biwaby.studytracker.utils.MapperUtils;

import org.biwaby.studytracker.models.TimetableItem;

import java.util.Comparator;

public class TimetableComparator implements Comparator<TimetableItem> {

    @Override
    public int compare(TimetableItem item1, TimetableItem item2) {
        int dateComparison = item1.getDate().compareTo(item2.getDate());

        if (dateComparison == 0) {
            int beginTimeComparison = item1.getBeginTime().compareTo(item2.getBeginTime());

            if (beginTimeComparison == 0) {
                return item1.getEndTime().compareTo(item2.getEndTime());
            }

            return beginTimeComparison;
        }

        return dateComparison;
    }
}
