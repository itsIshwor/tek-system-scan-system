package com.hackerrank.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.exception.ElementNotFoundException;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import com.hackerrank.api.service.ScanService;

@Service
public class DefaultScanService implements ScanService {
	private final ScanRepository scanRepository;
	private static final List<String> VALID_SORT_BY = List.of("id", "domainName", "numPages", "numBrokenLinks",
			"numMissingImages");

	DefaultScanService(ScanRepository scanRepository) {
		this.scanRepository = scanRepository;
	}

	@Override
	public List<Scan> getAllScan() {
		return scanRepository.findAll().stream().filter(scan -> !scan.isDeleted()).collect(Collectors.toList());
	}

	@Override
	public Scan createNewScan(Scan scan) {
		if (scan.getId() != null) {
			throw new BadRequestException("The ID must not be provided when creating a new Scan");
		}
		return scanRepository.save(scan);
	}

	@Override
	public Scan getScanById(Long id) {
		Optional<Scan> scanOptional = scanRepository.findById(id);
		if (scanOptional.isEmpty())
			throw new ElementNotFoundException("Can't find Scan with id: " + id);
		Scan scan = scanOptional.get();
		if (scan.isDeleted())
			throw new ElementNotFoundException("Can't find Scan with id: " + id);
		return scan;
	}

	@Override
	public void deleteById(Long id) {
		Scan sc = this.getScanById(id);
		sc.setDeleted(true);
		scanRepository.save(sc);
	}

	@Override
	public List<Scan> scanByProductName(String name, String OrderBy) {
		if (!VALID_SORT_BY.contains(OrderBy))
			throw new BadRequestException("invalid sorting column provided:" + OrderBy);

		Sort sort = Sort.by(Sort.Direction.ASC, OrderBy);
		return scanRepository.findAll(sort)
				.stream()
				.filter(sc -> sc.getDomainName().equals(name))
				.toList();
	}
}
