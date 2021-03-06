package uk.gov.digital.ho.hocs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.gov.digital.ho.hocs.api_lists.ListConsumerService;
import uk.gov.digital.ho.hocs.dto.DataListRecord;
import uk.gov.digital.ho.hocs.exception.EntityCreationException;
import uk.gov.digital.ho.hocs.exception.ListNotFoundException;
import uk.gov.digital.ho.hocs.model.DataList;
import uk.gov.digital.ho.hocs.model.DataListEntity;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j
public class DataListResource {
    private final DataListService dataListService;
    private final ListConsumerService listConsumerService;

    @Autowired
    public DataListResource(DataListService dataListService, ListConsumerService listConsumerService) {
        this.dataListService = dataListService;
        this.listConsumerService = listConsumerService;
    }

    @Deprecated
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity postList(@RequestBody DataList dataList) {
        log.info("Creating list \"{}\"", dataList.getName());
        try {
            dataListService.createList(dataList);
            return ResponseEntity.ok().build();
        } catch (EntityCreationException e) {
            log.info("List \"{}\" not created", dataList.getName());
            log.info(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/list/{name}", method = RequestMethod.POST)
    public ResponseEntity postListByName(@PathVariable("name") String name, @RequestBody Set<DataListEntity> dataListEntities) {
        log.info("Creating list \"{}\"", name);
        try {
            dataListService.createList(new DataList(name,dataListEntities));
            return ResponseEntity.ok().build();
        } catch (EntityCreationException e) {
            log.info("List \"{}\" not created", name);
            log.info(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/list/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DataListRecord> getListByName(@PathVariable("name") String name) {
        log.info("List \"{}\" requested", name);
        try {

            DataListRecord list;
            switch(name) {
                case "ukvi_member_list":
                    list = dataListService.getCombinedList("ukvi_member_list",
                            "commons_list",
                            "lords_list",
                            "scottish_parliament_list",
                            "northern_irish_assembly_list",
                            "european_parliament_list",
                            "welsh_assembly_list");
                    break;
                default:
                    list = dataListService.getListByName(name);
            }
            return ResponseEntity.ok(list);

        } catch (ListNotFoundException e){
            log.info("List \"{}\" not found", name);
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/list/{name}", method = RequestMethod.PUT)
    public ResponseEntity putListByName(@PathVariable("name") String name, @RequestBody Set<DataListEntity> dataListEntities) {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/api/refresh", method = RequestMethod.GET)
    public ResponseEntity refreshApiLists() {
        listConsumerService.refreshListsFromAPI();
        return ResponseEntity.ok().build();
    }

}