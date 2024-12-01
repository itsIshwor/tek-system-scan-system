package com.hackerrank.api.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class ScanControllerTest {
	ObjectMapper om = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ScanRepository repository;

	@Test
	public void testCreation() throws Exception {
		Scan expectedRecord = Scan.builder().domainName("java.com").build();
		Scan actualRecord = om.readValue(mockMvc
				.perform(post("/scan").contentType("application/json").content(om.writeValueAsString(expectedRecord)))
				.andDo(print()).andExpect(jsonPath("$.id", greaterThan(0))).andExpect(status().isCreated()).andReturn()
				.getResponse().getContentAsString(), Scan.class);

		Assertions.assertEquals(expectedRecord.getDomainName(), actualRecord.getDomainName());
	}

	@Test
	public void testGetProductSortByRandomName400Status() throws Exception {
		mockMvc.perform(get("/scan/search/java.com?orderBy=testColum")).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetProductSortByRandomName200Status() throws Exception {
		List<Scan> resultListByDomain = om
				.readValue(
						mockMvc.perform(get("/scan/search/domain1.com?orderBy=id")).andDo(print())
								.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
						new TypeReference<List<Scan>>() {
						});
		Assertions.assertTrue(resultListByDomain.size() > 0);
		List<Scan> otherScan = resultListByDomain.stream()
				.filter(result -> !result.getDomainName().equals("domain1.com")).toList();
		Assertions.assertTrue(otherScan.size() == 0);
	}

	@Test
	public void testCreationBadRequestExcpetion() throws Exception {
		Scan expectedRecord = Scan.builder().id(1L).domainName("java.com").build();
		mockMvc.perform(post("/scan").contentType("application/json").content(om.writeValueAsString(expectedRecord)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testFindById404Status() throws Exception {
		mockMvc.perform(get("/scan/-1")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testFindById200Status() throws Exception {
		Scan savedScan = getSavedScan();
		Scan actual = om.readValue(mockMvc.perform(get("/scan/" + savedScan.getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Scan.class);

		Assertions.assertEquals(savedScan.getDomainName(), actual.getDomainName());
		Assertions.assertEquals(savedScan.getId(), actual.getId());
	}

	@Test
	public void testDeleteById404Status() throws Exception {
		mockMvc.perform(delete("/scan/-1")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteById200Status() throws Exception {
		Scan savedScan = getSavedScan();
		mockMvc.perform(delete("/scan/" + savedScan.getId())).andDo(print()).andExpect(status().isOk());
		// once logical delete is performed, isDeleted should be true
		mockMvc.perform(get("/scan/" + savedScan.getId())).andDo(print()).andExpect(status().isNotFound()).andReturn();
		// Assertions.assertTrue(actual.isDeleted());

	}

	private Scan getSavedScan() {
		Scan scan = Scan.builder().domainName("hackerrank.com").numBrokenLinks(10).numMissingImages(2).numPages(300)
				.build();
		return repository.save(scan);
	}
}
