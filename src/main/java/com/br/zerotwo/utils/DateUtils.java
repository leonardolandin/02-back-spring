package com.br.zerotwo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

    public Date format(Date date) throws ParseException {
        String stringDate = simpleDateFormat.format(date);
        return simpleDateFormat.parse(stringDate);
    }
}
