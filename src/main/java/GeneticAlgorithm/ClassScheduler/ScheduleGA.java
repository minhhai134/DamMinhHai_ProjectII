package GeneticAlgorithm.ClassScheduler;

import GeneticAlgorithm.ClassScheduler.helper.UniversityClass;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;

/**
 *
 * @author ankit
 */
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
        GeneticAlgorithm ga = new GeneticAlgorithm(150, 0.02, 0.85, 3, 6);

        Population population = ga.initPopulation(schedule); // KHOI TAO NGAU NHIEN QUAN THE <- KHOI TAO QUAN THE <- KHOI TAO NGAU NHIEN CA THE
                                                                                                                    // DUA TREN SCHEDULE TRUYEN VAO
        ga.evalPopulation(population, schedule);   // DANH GIA QUAN THE BAN DAU

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, 1000) == false && ga.isTerminationConditionMet(population) == false) {
            // Print fitness
            LOG.info("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

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

// Print TaskList:
//        System.out.println();
//        UniversityClass classes[] = schedule.getClasses();
//        int classIndex = 1;
//        for (UniversityClass bestClass : classes) {
//            LOG.info("Class " + classIndex + ":");
//            LOG.info("Course: " + schedule.getCourse(bestClass.getCourseId()).getCourseName()+"\t");
//            LOG.info("Student Group: "  + schedule.getStudGroup(bestClass.getStudGroupId()).getStudGroupId()+"\t");
//            LOG.info("Class Room: "   + schedule.getClassRoom(bestClass.getClassRoomId()).getClassRoomNumber()+"\t");
//            LOG.info("Professor: " + schedule.getProfessor(bestClass.getProfessorId()).getProfessorName()+"\t");
//            LOG.info("Time: " + schedule.getTimeslot(bestClass.getTimeslotId()).getTimeslot()+"\t");
//            LOG.info("-----------------------------------------------------------");
//            classIndex++;
//        }


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

