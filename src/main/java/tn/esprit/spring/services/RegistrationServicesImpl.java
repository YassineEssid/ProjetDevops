package tn.esprit.spring.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServicesImpl.class);

    @Autowired
    private IRegistrationRepository registrationRepository;
    @Autowired
    private ISkierRepository skierRepository;
    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Course course = courseRepository.findById(numCours).orElse(null);

        if (skier == null || course == null) {
            return null;
        }

        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            logger.info("Sorry, you're already registered to this course for week: " + registration.getNumWeek());
            return null;
        }

        int ageSkieur = Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
        logger.info("Age: " + ageSkieur);

        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                logger.info("Add without tests");
                return assignRegistration(registration, skier, course);

            case COLLECTIVE_CHILDREN:
                if (ageSkieur < 16) {
                    logger.info("Ok CHILD !");
                    if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                        logger.info("Course successfully added!");
                        return assignRegistration(registration, skier, course);
                    } else {
                        logger.info("Full Course! Please choose another week to register.");
                        return null;
                    }
                } else {
                    logger.info("Sorry, your age doesn't allow you to register for this course! Try a Collective Adult Course.");
                }
                break;

            default:
                if (ageSkieur >= 16) {
                    logger.info("Ok ADULT !");
                    if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                        logger.info("Course successfully added!");
                        return assignRegistration(registration, skier, course);
                    } else {
                        logger.info("Full Course! Please choose another week to register.");
                        return null;
                    }
                }
                logger.info("Sorry, your age doesn't allow you to register for this course! Try a Collective Child Course.");
        }
        return registration;
    }

    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
