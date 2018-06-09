package com.generation_p.hotel_demo.views.convertor;

import java.time.Duration;
import java.time.LocalDate;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

@SuppressWarnings("serial")
public class DateConverter implements Converter<LocalDate, Long> {

	@Override
	public Result<Long> convertToModel(LocalDate value, ValueContext context) {
		if (value == null) {
			Result.ok(null);
		}
		long days = Duration.between(value.atTime(0, 0), LocalDate.now().atTime(0, 0)).toDays();
		return Result.ok(days);
	}

	@Override
	public LocalDate convertToPresentation(Long value, ValueContext context) {
		if (value == null) {
			return LocalDate.now();
		}
		return LocalDate.now().minusDays(value);
	}

}
