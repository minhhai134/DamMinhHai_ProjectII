package GeneticAlgorithm.ClassScheduler;

import GeneticAlgorithm.ClassScheduler.helper.TaskList;
import org.apache.log4j.Logger;

import java.io.IOException;


public class ScheduleGA {

      private static final Logger LOG = Logger.getLogger(ScheduleGA.class);


    public static void main(String[] args)  {
        Schedule schedule = null;  // Khoi tao mot Schedule khoi dau (1)
        try {
            schedule = DataLoader.initializeSchedule();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(60, 0.01, 0.85, 2, 6);

        Population population = ga.initPopulation(schedule); // KHOI TAO NGAU NHIEN QUAN THE <- KHOI TAO QUAN THE <- KHOI TAO NGAU NHIEN CA THE
                                                                                                                    // DUA TREN SCHEDULE TRUYEN VAO
        ga.evalPopulation(population, schedule);   // DANH GIA QUAN THE BAN DAU

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, 1000) == false && ga.isTerminationConditionMet(population) == false) {
           //
            // Print fitness
            LOG.info("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

            // Dat lai gia tri timeLimit
            schedule.setTimeLimit(population.getFittest(0).getTotalTime());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, schedule);  // schedule nay co thay doi khong, va thay doi o cho nao?
                                                                     // Thay doi nhu the nao?
            // Evaluate population
            ga.evalPopulation(population, schedule);

            // Increment the current generation
            generation++;

//            LOG.info("");
//
//            LOG.info("");
        }

        // Print fitness
        schedule.createTaskList(population.getFittest(0));
        LOG.info("");
        LOG.info("Schedule found after " + generation + " generations");
        LOG.info("Fitness of final Schedule: " + population.getFittest(0).getFitness());
        LOG.info("Conflict: " + schedule.calcClashes());
        LOG.info("Total time: " );
        LOG.info("Total cost: " );

 //Print TaskList:
        System.out.println();
        TaskList taskslist[] = schedule.getTasksListAsArray();
        int classIndex = 1;
        for (TaskList bestTask : taskslist) {
            LOG.info("Task " + classIndex + ":");
            LOG.info("TaskID: " + bestTask.getTaskID() +"\t");
            LOG.info("ResourceID: "  + bestTask.getResourceID() +"\t");
            LOG.info("Start Time: "   + bestTask.getStartTime() +"\t");
            LOG.info("End Time: " + bestTask.getEndTime() +"\t");
//            LOG.info("Time: " + schedule.getTimeslot(bestClass.getTimeslotId()).getTimeslot()+"\t");
            LOG.info("-----------------------------------------------------------");
            classIndex++;
        }
        System.out.println("Time limit: "+schedule.calcTimeLimit());
        System.out.println("Total time: "+schedule.calcDurationTime());
        System.out.println(schedule.calcClashes());
        System.out.println(schedule.calc1());
        System.out.println(schedule.calc2());

        for (TaskList bestTask : taskslist) {
            System.out.print("TaskID: " + bestTask.getTaskID() +" ");
            if(bestTask.getTaskID()>=10) System.out.print(" ");
            else System.out.print(" "+ " ");
            for(int i=0;i<bestTask.getStartTime();i++) {System.out.print(" ");}
            for(int i=bestTask.getStartTime();i<=bestTask.getEndTime();i++){  System.out.print(bestTask.getResourceID());}
            System.out.print("\n");

        }



// CAI NAY TU VIET DE TEST:
//        for (UniversityClass classA : schedule.getClasses()){
//            for (UniversityClass classB : schedule.getClasses()) {
//                if (classA.getProfessorId() == classB.getProfessorId() && classA.getTimeslotId() == classB.getTimeslotId()
//                        && classA.getClassId() != classB.getClassId()) {
////                  clashes++;
//                    System.out.println(classA.getProfessorId());
//                    System.out.println(classA.getTimeslotId());
////                    JOptionPane.showMessageDialog(null,"Wrong"+ classA.getProfessorId() + classA.getTimeslotId() );
////                    break;
//                }
//
//            }
//        }

    }


}

