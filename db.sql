SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";





CREATE TABLE Film (
                      id int(11) NOT NULL,
                      titre varchar(40) NOT NULL,
                      id_rea int(11) NOT NULL,
                      PRIMARY KEY (id,id_rea)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



INSERT INTO Film (id, titre, id_rea) VALUES
                                         (1, 'Arche perdue', 1),
                                         (2, 'Alien', 2),
                                         (3, 'Temple Maudit', 1),
                                         (4, 'Blade Runner', 2),
                                         (5, 'Alien3', 4),
                                         (6, 'Fight Club', 4),
                                         (7, 'Orange Mecanique', 3);



CREATE TABLE Personne (
                          id int(11) NOT NULL,
                          nom varchar(40) NOT NULL,
                          prenom varchar(40) NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO Personne (id, nom, prenom) VALUES
                                           (1, 'Spielberg', 'Steven'),
                                           (2, 'Scott', 'Ridley'),
                                           (3, 'Kubrick', 'Stanley'),
                                           (4, 'Fincher', 'David');






ALTER TABLE Film
    MODIFY id int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;


ALTER TABLE Personne
    MODIFY id int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;


ALTER TABLE Film
    ADD CONSTRAINT film_ibfk_1 FOREIGN KEY (id_rea) REFERENCES Personne (id);
COMMIT;
