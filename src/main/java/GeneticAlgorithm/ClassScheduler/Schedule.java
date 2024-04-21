package GeneticAlgorithm.ClassScheduler;

import GeneticAlgorithm.ClassScheduler.helper.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    private static  int cnt=0;
    private final HashMap<Integer, Resource> resources;
    private final HashMap<Integer, Task> tasks;


    public Schedule(HashMap<Integer, Resource> resources, HashMap<Integer, Task> tasks) {
        this.resources = resources;
        this.tasks = tasks;
        timeLimit = calcTimeLimit();
    }

    public Schedule(){
        this.resources = new HashMap<>();
        this.tasks = new HashMap<>();
        timeLimit = calcTimeLimit();
    }

    public Schedule(Schedule schedule){
        this.resources = schedule.getResources();
        this.tasks = schedule.getTasks();
        timeLimit = calcTimeLimit();
//        if(cnt<10) JOptionPane.showMessageDialog(null,timeLimit); cnt++;
    }


    public HashMap<Integer, Resource> getResources() {
        return resources;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }



    // add new Task
    public void addTask(int taskID, int duration, HashMap<String, Integer> requiredSkills, ArrayList<Integer> predecessors) {
        this.tasks.put(taskID, new Task(taskID, duration,requiredSkills,predecessors ));
    }

     // add new Resource
    public void addResource(int resourceId, double salary, HashMap<String, Integer> skills) {
        this.resources.put(resourceId, new Resource(resourceId, salary, skills));
    }

    public int getNumofTasks(){ return this.tasks.size();}

    public Task[] getTasksAsArray() {
        return (Task[]) this.tasks.values().toArray(new Task[this.tasks.size()]);
    }


    public void createTaskList(Individual individual) {
        // Init tasksList
        HashMap<Integer,TaskList>  tasksList = new HashMap<>();

        // Get individual’s chromosome
        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int taskListIndex = 0;

        for (Task task : this.getTasksAsArray()) {
//            int taskIds[] = task.getCourseIds();

            tasksList.put(task.getTaskID(),new TaskList(taskListIndex, task.getTaskID(), task.getDuration())) ;

                // Add startTime
                tasksList.get(task.getTaskID()).setStartTime(chromosome[chromosomePos]);
                chromosomePos++;

                // Add ResourceID
                tasksList.get(task.getTaskID()).setResourceID(chromosome[chromosomePos]);
                chromosomePos++;

                taskListIndex++;

        }

        this.tasksList = tasksList;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int calcClashes() {
        int clashes = 0;

        for (Integer taskListID : this.tasksList.keySet()) {

            // Check predecessors
            ArrayList<Integer> predecessors = tasks.get(taskListID).getPredecessors(); // Lay ra cac task tien nhiem
            for(int i = 0; i < predecessors.size(); i++) {  // duyet tung task tien nhiem
               if(tasksList.get(predecessors.get(i)).getEndTime()>= tasksList.get(taskListID).getStartTime()) clashes++;
            }



            // Check resource available

//            for (TaskList taskListB : this.tasksList) {
////                if (classA.getClassRoomId() == classB.getClassRoomId() && classA.getTimeslotId() == classB.getTimeslotId()
////                        && classA.getClassId() != classB.getClassId()) {
////                    clashes++;
////                    break;
////                }
//            }
        }
//        if(cnt<10){ JOptionPane.showMessageDialog(null,clashes);} cnt++;
        return clashes;

    }


    private HashMap<Integer,TaskList>  tasksList;

    private  int timeLimit;

    public int calcTimeLimit(){
        int T=0;
        if(cnt<=0){

        }

        for(Integer taskID : tasks.keySet()){

             T+=tasks.get(taskID).getDuration();
//            if(cnt<10) JOptionPane.showMessageDialog(null,taskID +" "+ tasks.get(taskID).getDuration() +" "+ T); cnt++;
        }
//        if(cnt<10) JOptionPane.showMessageDialog(null, T); cnt++;
        return 100;
    }


    public int getRandomStartTime() {

        return (int) (timeLimit * Math.random());

    }

}
