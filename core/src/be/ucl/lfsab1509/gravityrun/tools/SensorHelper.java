package be.ucl.lfsab1509.gravityrun.tools;

public interface SensorHelper {
    /**
     * Choix du capteur pour déterminer l'orientation de l'appareil.
     *
     * - ORIENTATION : utilisation des capteurs liés à l'orientation, en particulier le capteur du rotation vector et de la rotation matrix.
     * - GRAVITY : utilisation du capteur déterminant l'orientation de la gravité, qui lui-même tire parti de l'accéléromètre, du magnétomètre et éventuellement du gyroscope.
     *   Avantages : logiquement, il est plus stable que les autres.
     *   Inconvénients : si l'accélération subie par l'appareil est significative et dure suffisament longtemps, l'axe de la gravité sera temporairement fortement déréglé.
     */
    enum OrientationSensorType {
        ORIENTATION,
        GRAVITY;
    }

    /**
     * Choix du capteur pour la mesure de l'accélération angulaire, pour le gyroscope.
     *
     * - GYROSCOPE : utiliser directement les données du gyroscope. Le gyroscope peut être absent sur certaines plateformes, et sa précision/calibration peut varier.
     * - ORIENTATION_DERIVED : utiliser les données du capteur pour l'orientation, en dérivant les données.
     */
    enum GyroscopeSensorType {
        GYROSCOPE,
        ORIENTATION_DERIVED;
    }

    float getGyroscopeY();

    /**
     * Retourne un vecteur de taille 2 représentant la direction de la gravité par rapport à l'appareil.
     * La valeur en 0 est l'accélération normalisée dans l'axe x (horizontal vers la droite pour la majorité des devices),
     * la valeur en 1 est dans l'axe y (vertical vers le haut).
     * Les vecteurs sont normalisés par rapport à l'accélération gravitationnelle mesurée.
     * Par conséquent, la norme de ce vecteur n'est pas nécessairement de 1 ; elle vaut 0
     * si l'appareil est à plat, et 1 si la gravité est dans le plan de l'écran.
     *
     * @return le vecteur décrit ci-dessus.
     */
    float[] getGravityDirectionVector();

    /**
     * Retourne un vecteur de taille 2 représentant la vitesse du changement du vecteur retourné par {@link SensorHelper#getGravityDirectionVector()}.
     *
     * @return Voir la description...
     */
    float[] getVelocityVector();

    /**
     * Retourne true si l'utilisateur a fait sauté la bille dans les quelques millisecondes précédentes, false sinon.
     * Lors d'un saut, seul le premier appel à cette fonction retournera true, les suivants retourneront nécessairement false, jusqu'au prochain saut.
     * Il est possible qu'un délai soit présent entre le saut détecté et le prochain saut qui pourra être détecté et qui sera reporté.
     *
     * @return true si un saut a été détecté et n'a pas encore été rapporté, false sinon.
     */
    boolean hasJumped();

    /**
     * Met les différents capteurs utilisés par l'activité en pause, afin d'économiser les ressources.
     */
    void pauseSensors();

    /**
     * Rétablit les différents capteurs utilisés par l'activité.
     */
    void resumeSensors();

    void setOrientationSensorType(OrientationSensorType type);

    void setGyroscopeSensorType(GyroscopeSensorType type);
}
