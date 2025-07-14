package Lesson11.Task2;

import Lesson11.Task2.Obstacles.Obstacle;
import Lesson11.Task2.Participants.Participant;

public class Competition {
    public void start(Participant[] participants, Obstacle[] obstacles){
        for (Participant participant : participants){
            System.out.printf("Участник %s начинает прохождение препятствий.\n", participant.getName());
            boolean failed = false;
            for (Obstacle obstacle : obstacles){
                int result = obstacle.overcome(participant);
                String obstacleName = obstacle.getDescription();
                String obstacleMeasure = obstacle.getMeasure();
                int obstacleValue = obstacle.getValue();
                if (result >= obstacleValue){
                    System.out.printf("Участник %s прошел препятствие %s на дистанции %s %s.\n",
                            participant.getName(), obstacleName, obstacleValue, obstacleMeasure);
                }
                else {
                    System.out.printf("Участник %s не прошел препятствие %s на дистанции %s %s. Пройдено %s %s.\n",
                            participant.getName(), obstacleName, obstacleValue,obstacleMeasure, result, obstacleMeasure);
                    failed = true;
                    break;
                }
            }
            if (!failed){
                System.out.printf("Участник %s успешно прошел все препятствия!\n", participant.getName());
            }
            System.out.println();
        }
    }
}
