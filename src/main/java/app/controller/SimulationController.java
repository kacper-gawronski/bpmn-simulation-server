package app.controller;

import app.dto.Process;
import app.dto.*;
import app.repository.Repository;
import app.service.FlowableService;
import app.service.ParseService;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SimulationController {


    ParseService parseService;
    FlowableService flowableService;

    // -------------------------------------------------------------

    @PostMapping(value = "/file-name", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> setFileName(@RequestBody String fileName) {
        Repository.setFileName(fileName);
        return ResponseEntity.ok().body("File name: " + fileName + " is set on the server");
    }

    @PostMapping(value = "/parse", consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map<String, Object>> parseModel(@RequestBody String file) {
        Model model = new Model(parseService.saveModelToFile(file, Repository.getFileName()), file);

        Process process = parseService.setProcessParameters(model);
        Variables variables = parseService.getVariables(model);
        List<TaskDetail> taskDetails = parseService.getTasksDetails(model);

        Repository.setModel(model);
        Repository.setProcess(process);
        Repository.setVariables(variables);
        Repository.setTaskDetails(taskDetails);

        Map<String, Object> modelProperties = new HashMap<>();
        modelProperties.put("process", process);
        modelProperties.put("variables", variables);
        modelProperties.put("tasks", taskDetails);

        return ResponseEntity.ok().body(modelProperties);
    }

//    @GetMapping(value = "/deploy")
//    public ResponseEntity<Pair<String, String>> deployProcessDefinition() {
//        String deploymentName = flowableService.deployProcessDefinition();
//        return ResponseEntity.ok().body(new Pair<>("deploymentName", deploymentName));
//    }

    @PostMapping(value = "/number-of-simulations")
    public ResponseEntity<Integer> setNumberOfSimulations(@RequestBody Integer numberOfSimulations) {
        Repository.setNumberOfSimulations(numberOfSimulations);
        return ResponseEntity.ok().body(Repository.getNumberOfSimulations());
    }

    @PostMapping(value = "/variables", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Variables> setVariables(@RequestBody Map<String, Map<Object, Integer>> variablesWithProbabilities) {
        Repository.setVariables(new Variables(Repository.getVariables().getPossibleVariables(), variablesWithProbabilities));
        return ResponseEntity.ok().body(Repository.getVariables());
    }

    @PostMapping(value = "/tasks-values")
    public ResponseEntity<List<TaskDetail>> setTasksValues(@RequestBody List<TaskDetail> taskDetails) {
        Repository.setTaskDetails(taskDetails);
        return ResponseEntity.ok().body(taskDetails);
    }

    @GetMapping("/simulation")
    public ResponseEntity<Map<String, Object>> deployAndSimulateProcess() {
        flowableService.deployProcessDefinition();

        Repository.setAllSimulations(new ArrayList<>());

        int sumDuration = 0;
        double sumCost = 0;
        for (int i = 0; i < Repository.getNumberOfSimulations(); i++) {
            SimulationActivities simulationResult = flowableService.simulateProcessDefinition();
            sumDuration += simulationResult.getTotalDuration();
            sumCost += simulationResult.getTotalCost();
        }

        Repository.setSumOfDurations(sumDuration);
        Repository.setSumOfCosts(sumCost);

        Map<String, Object> result = new HashMap<>();
        result.put("processInstances", Repository.getAllSimulations());
        result.put("sumOfDurations", Repository.getSumOfDurations());
        result.put("sumOfCosts", Repository.getSumOfCosts());

        return ResponseEntity.ok().body(result);
    }

}
