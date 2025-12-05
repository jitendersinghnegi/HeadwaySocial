package headway.backend.service;

import headway.backend.dto.dashboard.HotelMonthValue;

import java.util.List;

public interface HotelComparisonService {
    public List<HotelMonthValue> getComparison(int year, String metric);
}
