package com.hackerrank.api.service;

import com.hackerrank.api.model.Scan;
import java.util.List;

public interface ScanService {

  List<Scan> getAllScan();

  Scan createNewScan(Scan scan);

  Scan getScanById(Long id);

  void deleteById(Long id);

  List<Scan> scanByProductName(String name, String OrderBy);
}
