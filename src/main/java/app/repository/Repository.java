package app.repository;

import app.dto.Process;
import app.dto.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class Repository {

    static Model model;
    static Process process;
    static Variables variables;
    static Map<String, Object> inputVariables;
    static List<TaskDetail> taskDetails;
    static SimulationActivities simulationActivities;

    public static Model getModel() {
        return model;
    }

    public static void setModel(Model model) {
        Repository.model = model;
    }

    public static Process getProcess() {
        return process;
    }

    public static void setProcess(Process process) {
        Repository.process = process;
    }

    public static Variables getVariables() {
        return variables;
    }

    public static void setVariables(Variables variables) {
        Repository.variables = variables;
    }

    public static Map<String, Object> getInputVariables() {
        return inputVariables;
    }

    public static void setInputVariables(Map<String, Object> inputVariables) {
        Repository.inputVariables = inputVariables;
    }

    public static List<TaskDetail> getTaskDetails() {
        return taskDetails;
    }

    public static void setTaskDetails(List<TaskDetail> taskDetails) {
        Repository.taskDetails = taskDetails;
    }

    public static SimulationActivities getSimulationActivities() {
        return simulationActivities;
    }

    public static void setSimulationActivities(SimulationActivities simulationActivities) {
        Repository.simulationActivities = simulationActivities;
    }
}