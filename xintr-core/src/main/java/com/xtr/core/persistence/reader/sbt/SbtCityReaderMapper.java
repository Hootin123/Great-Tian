package com.xtr.core.persistence.reader.sbt;

import com.xtr.comm.sbt.api.City;

import java.util.List;

public interface SbtCityReaderMapper {

    List<City> selectCities();
}