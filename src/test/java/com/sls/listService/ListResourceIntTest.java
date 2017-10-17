package com.sls.listService;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ListResourceIntTest {

    @Autowired
    private ListRepository repository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        repository.deleteAll();

        Set<DataListEntity> subList = new HashSet<>();
        subList.add(new DataListEntity("SubValue","sub_ref"));

        Set<DataListEntityProperties> properties = new HashSet<>();
        properties.add(new DataListEntityProperties("Key", "Value"));

        Set<DataListEntity> list = new HashSet<>();
        list.add(new DataListEntity("TopValue","top_ref", subList, properties));

        DataList datalist = new DataList("TestListTwo", list);

        repository.save(datalist);

    }

    @Test
    public void shouldRetrieveAllEntities() throws IOException, JSONException {
        String auditRecords = restTemplate.getForObject("/list/TestListTwo", String.class);
        String expectedRecords = IOUtils.toString(getClass().getResourceAsStream("/expected.json"));

        JSONAssert.assertEquals(auditRecords, expectedRecords, false);
    }
}