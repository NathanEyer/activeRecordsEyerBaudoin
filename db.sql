/* Script de création de la table */

/* SET */
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/* Création des tables */

DROP TABLE IF EXISTS Film;
DROP TABLE IF EXISTS Personne;

CREATE TABLE Personne (
                          id int(11) NOT NULL AUTO_INCREMENT,
                          nom varchar(40) NOT NULL,
                          prenom varchar(40) NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE Film (
                      id int(11) NOT NULL AUTO_INCREMENT,
                      titre varchar(40) NOT NULL,
                      id_rea int(11) NOT NULL,
                      PRIMARY KEY (id),
                      FOREIGN KEY (id_rea) REFERENCES Personne (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

COMMIT;

/* Insertions */
INSERT INTO Personne (nom, prenom) VALUES
                                       ('Spielberg', 'Steven'),
                                       ('Scott', 'Ridley'),
                                       ('Kubrick', 'Stanley'),
                                       ('Fincher', 'David');

INSERT INTO Film (titre, id_rea) VALUES
                                     ('Arche perdue', 1),
                                     ('Alien', 2),
                                     ('Temple Maudit', 1),
                                     ('Blade Runner', 2),
                                     ('Alien3', 4),
                                     ('Fight Club', 4),
                                     ('Orange Mecanique', 3);