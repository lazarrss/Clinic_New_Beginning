-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 29, 2025 at 08:45 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `novi_pocetak`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `debt1` (IN `therapist_id` INT)   BEGIN
    DECLARE final_amount DECIMAL(10, 2);

    START TRANSACTION;
    -- Prvo izračunajte final_amount
    SELECT 
        CASE 
            WHEN p.Valuta_valuta_id NOT IN ('RSD', 'EUR') THEN ROUND(c.trenutna_cena * is.novi_kurs * 1.05, 2)
            WHEN p.Valuta_valuta_id = 'EUR' THEN ROUND(c.trenutna_cena * is.novi_kurs)
            ELSE c.trenutna_cena
        END
    INTO final_amount
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN istorija_promene ip on ip.Valuta_valuta_id = p.Valuta_valuta_id 
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id LIMIT 1; -- Limitirajte da bi dobili samo jedan rezultat

    -- Sada možete koristiti final_amount u sledećem SELECT-u
    SELECT 
        k.ime,
        k.broj_telefona,
        p.svrha,
        p.iznos,
        p.nacin_placanja,
        p.datum_placanja,
        p.Valuta_valuta_id,
        CASE 
            WHEN p.iznos = final_amount 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 1 and p.iznos >= final_amount * 0.3 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 2 AND prva_rata.datum_placanja IS NOT NULL 
                 AND prva_rata.datum_placanja > DATE_ADD(prva_rata.datum_placanja, INTERVAL 30 DAY)
            THEN 'No'
            ELSE 'Yes'
        END AS is_overdue,
        CASE 
            WHEN p.datum_placanja > DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE) 
            THEN DATEDIFF(p.datum_placanja, s.dan_vreme)
            ELSE 0
        END AS days_overdue
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN Prijava pr ON pr.Seansa_seansa_id = s.seansa_id
    LEFT JOIN Klijent k ON k.klijent_id = pr.klijent_id
    LEFT JOIN Placanje prva_rata 
    ON prva_rata.Seansa_seansa_id = p.Seansa_seansa_id AND p.rata = 1
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id;

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `debt2` (IN `therapist_id` INT)   BEGIN
    DECLARE final_amount DECIMAL(10, 2);

    START TRANSACTION;
    -- Prvo izračunajte final_amount
    SELECT 
        CASE 
            WHEN p.Valuta_valuta_id NOT IN ('RSD', 'EUR') THEN ROUND(c.trenutna_cena * ip.novi_kurs * 1.05, 2)
            WHEN p.Valuta_valuta_id = 'EUR' THEN ROUND(c.trenutna_cena * ip.novi_kurs)
            ELSE c.trenutna_cena
        END
    INTO final_amount
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN istorija_promene ip on ip.Valuta_valuta_id = p.Valuta_valuta_id 
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id LIMIT 1; -- Limitirajte da bi dobili samo jedan rezultat

    -- Sada možete koristiti final_amount u sledećem SELECT-u
    SELECT 
        k.ime,
        k.broj_telefona,
        p.svrha,
        p.iznos,
        p.nacin_placanja,
        p.datum_placanja,
        p.Valuta_valuta_id,
        CASE 
            WHEN p.iznos = final_amount 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 1 and p.iznos >= final_amount * 0.3 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 2 AND prva_rata.datum_placanja IS NOT NULL 
                 AND p.datum_placanja > DATE_ADD(prva_rata.datum_placanja, INTERVAL 30 DAY)
            THEN 'No'
            ELSE 'Yes'
        END AS is_overdue,
        CASE 
            WHEN p.datum_placanja > DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE) 
            THEN DATEDIFF(p.datum_placanja, s.dan_vreme)
            ELSE 0
        END AS days_overdue
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN Prijava pr ON pr.Seansa_seansa_id = s.seansa_id
    LEFT JOIN Klijent k ON k.klijent_id = pr.klijent_id
    LEFT JOIN Placanje prva_rata 
    ON prva_rata.Seansa_seansa_id = p.Seansa_seansa_id AND p.rata = 1
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id;

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_objavljivanje_podataka` (IN `p_datum` DATE, IN `p_kome` VARCHAR(255), IN `p_vrsta_id` INT, IN `p_seansa_id` INT)   BEGIN
    START TRANSACTION;

    INSERT INTO objavljivanje_podataka (
        datum_objavljivanja, 
        kome_je_objavljeno, 
        Vrsta_objavljivanja_vrsta_objavljivanja_id, 
        Seansa_seansa_id
    )
    VALUES (p_datum, p_kome, p_vrsta_id, p_seansa_id);

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_psihoterapeut` (IN `p_ime` VARCHAR(100), IN `p_prezime` VARCHAR(100), IN `p_jmbg` CHAR(13), IN `p_datum` DATE, IN `p_prebivaliste` VARCHAR(100), IN `p_broj` VARCHAR(20), IN `p_psiholog` BOOLEAN)   BEGIN
    START TRANSACTION;

    INSERT INTO psihoterapeut (
        ime, prezime, JMBG, datum_rodjenja,
        prebivaliste, broj_telefona, psiholog
    )
    VALUES (p_ime, p_prezime, p_jmbg, p_datum, p_prebivaliste, p_broj, p_psiholog);

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_vrsta_objavljivanja` (IN `p_id` INT, IN `p_naziv` VARCHAR(255))   BEGIN
    START TRANSACTION;

    INSERT INTO vrsta_objavljivanja (vrsta_objavljivanja_id, naziv)
    VALUES (p_id, p_naziv);

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `paymentDebts` (IN `therapist_id` INT)   BEGIN
    START TRANSACTION;
	
    SELECT 
        k.ime,
        k.broj_telefona,
        p.svrha,
        p.iznos,
        p.nacin_placanja,
        p.datum_placanja,
        p.Valuta_valuta_id,
        CASE 
        WHEN p.Valuta_valuta_id NOT IN ('RSD', 'EUR') THEN ROUND(c.trenutna_cena * ip.novi_kurs * 1.05, 2)
        WHEN p.Valuta_valuta_id = 'EUR' THEN ROUND(c.trenutna_cena * ip.novi_kurs)
        ELSE c.trenutna_cena
    	END AS final_amount,
        CASE 
            WHEN p.iznos = final_amount 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 1 and p.iznos >= final_amount * 0.3 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 2 AND prva_rata.datum_placanja IS NOT NULL 
     		AND prva_rata.datum_placanja > DATE_ADD(prva_rata.datum_placanja, INTERVAL 30 DAY)
			THEN 'No'
            ELSE 'Yes'
        END AS is_overdue,
        CASE 
            WHEN p.datum_placanja > DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE) 
            THEN DATEDIFF(p.datum_placanja, s.dan_vreme)
            ELSE 0
        END AS days_overdue
        
        
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN Prijava pr ON pr.Seansa_seansa_id = s.seansa_id
    LEFT JOIN Klijent k ON k.klijent_id = pr.klijent_id
    LEFT JOIN istorija_promene ip on ip.Valuta_valuta_id = p.Valuta_valuta_id 
    LEFT JOIN Placanje prva_rata 
    ON prva_rata.Seansa_seansa_id = p.Seansa_seansa_id AND pl.rata = 1
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id;

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `pregled_uplata_dugovanja` (IN `therapist_id` INT)   BEGIN
    START TRANSACTION;
	
    SELECT 
        k.ime,
        k.broj_telefona,
        p.svrha,
        p.iznos,
        p.nacin_placanja,
        p.datum_placanja,
        p.Valuta_valuta_id,
        CASE 
        WHEN p.Valuta_valuta_id NOT IN ('RSD', 'EUR') THEN ROUND(c.trenutna_cena * is.novi_kurs * 1.05, 2)
        WHEN p.Valuta_valuta_id = 'EUR' THEN ROUND(c.trenutna_cena * is.novi_kurs)
        ELSE c.trenutna_cena
    	END AS final_amount,
        CASE 
            WHEN p.iznos = final_amount 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 1 and p.iznos >= final_amount * 0.3 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 2 AND prva_rata.datum_placanja IS NOT NULL 
     		AND prva_rata.datum_placanja > DATE_ADD(prva_rata.datum_placanja, INTERVAL 30 DAY)
			THEN 'No'
            ELSE 'Yes'
        END AS is_overdue,
        CASE 
            WHEN p.datum_placanja > DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE) 
            THEN DATEDIFF(p.datum_placanja, s.dan_vreme)
            ELSE 0
        END AS days_overdue
        
        
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cenaa c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN Prijava pr ON pr.Seansa_seansa_id = s.seansa_id
    LEFT JOIN Klijent k ON k.klijent_id = pr.klijent_id
    LEFT JOIN istorija_promene ip on ip.Valuta_valuta_id = p.Valuta_valuta_id 
    LEFT JOIN Placanje prva_rata 
    ON prva_rata.Seansa_seansa_id = p.Seansa_seansa_id AND pl.rata = 1
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id;

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_psihoterapeut` (IN `p_ime` VARCHAR(100), IN `p_prezime` VARCHAR(100), IN `p_jmbg` CHAR(13), IN `p_datum` DATE, IN `p_prebivaliste` VARCHAR(100), IN `p_broj` VARCHAR(20), IN `p_psiholog` BOOLEAN, IN `p_id` INT)   BEGIN
    START TRANSACTION;

    UPDATE psihoterapeut
    SET 
        ime = p_ime,
        prezime = p_prezime,
        JMBG = p_jmbg,
        datum_rodjenja = p_datum,
        prebivaliste = p_prebivaliste,
        broj_telefona = p_broj,
        psiholog = p_psiholog
    WHERE psihoterapeut_id = p_id;

    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `uplata_i_dugovanja` (IN `therapist_id` INT)   BEGIN
    START TRANSACTION;
	
    SELECT 
        k.ime,
        k.broj_telefona,
        p.svrha,
        p.iznos,
        p.nacin_placanja,
        p.datum_placanja,
        p.Valuta_valuta_id,
        CASE 
        WHEN p.Valuta_valuta_id NOT IN ('RSD', 'EUR') THEN ROUND(c.trenutna_cena * is.novi_kurs * 1.05, 2)
        WHEN p.Valuta_valuta_id = 'EUR' THEN ROUND(c.trenutna_cena * is.novi_kurs)
        ELSE c.trenutna_cena
    	END AS final_amount,
        CASE 
            WHEN p.iznos = final_amount 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 1 and p.iznos >= final_amount * 0.3 
                 AND p.datum_placanja < DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE)
            THEN 'No'
            WHEN p.rata = 2 AND prva_rata.datum_placanja IS NOT NULL 
     		AND prva_rata.datum_placanja > DATE_ADD(prva_rata.datum_placanja, INTERVAL 30 DAY)
			THEN 'No'
            ELSE 'Yes'
        END AS is_overdue,
        CASE 
            WHEN p.datum_placanja > DATE_ADD(s.dan_vreme, INTERVAL 120 MINUTE) 
            THEN DATEDIFF(p.datum_placanja, s.dan_vreme)
            ELSE 0
        END AS days_overdue
        
        
    FROM Placanje p
    LEFT JOIN Seansa s ON s.seansa_id = p.Seansa_seansa_id
    LEFT JOIN Cena c ON s.Cena_cena_id = c.cena_id
    LEFT JOIN Prijava pr ON pr.Seansa_seansa_id = s.seansa_id
    LEFT JOIN Klijent k ON k.klijent_id = pr.klijent_id
    LEFT JOIN istorija_promene ip on ip.Valuta_valuta_id = p.Valuta_valuta_id 
    LEFT JOIN Placanje prva_rata 
    ON prva_rata.Seansa_seansa_id = p.Seansa_seansa_id AND pl.rata = 1
    WHERE s.Psihoterapeut_psihoterapeut_id = therapist_id;

    COMMIT;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `cena`
--

CREATE TABLE `cena` (
  `cena_id` int(11) NOT NULL,
  `datum_promene` date DEFAULT NULL,
  `trenutna_cena` decimal(7,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cena`
--

INSERT INTO `cena` (`cena_id`, `datum_promene`, `trenutna_cena`) VALUES
(5, NULL, 5000.00),
(8, NULL, 9000.00),
(7, '2022-09-30', 8800.00),
(3, '2022-12-05', 7000.00),
(1, '2023-01-10', 7500.00),
(2, '2023-03-15', 6000.00),
(6, '2023-07-25', 7500.00),
(10, '2023-11-11', 10000.00),
(4, '2024-02-20', 5500.00),
(9, '2024-04-01', 8200.00);

-- --------------------------------------------------------

--
-- Table structure for table `centar_za_obuku`
--

CREATE TABLE `centar_za_obuku` (
  `centar_id` int(11) NOT NULL,
  `naziv` varchar(50) NOT NULL,
  `email` varchar(40) NOT NULL,
  `postanska_adresa` varchar(60) NOT NULL,
  `broj_telefona` char(13) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `centar_za_obuku`
--

INSERT INTO `centar_za_obuku` (`centar_id`, `naziv`, `email`, `postanska_adresa`, `broj_telefona`) VALUES
(1, 'Centar za edukaciju psihoterapeuta', 'info@cep.rs', 'Bulevar kralja Aleksandra 73, Beograd', '0601234567'),
(2, 'Institut za psihoterapijske studije', 'kontakt@ips.edu.rs', 'Narodnog fronta 25, Novi Sad', '0612345678'),
(3, 'Akademija za psihoterapiju', 'akademija@psihoterapija.rs', 'Cara Dušana 44, Niš', '0623456789'),
(4, 'Centar za kognitivno-bihevioralnu terapiju', 'cbt@edukacija.rs', 'Kneza Miloša 12, Kragujevac', '0634567890'),
(5, 'Psihoterapijski trening centar', 'ptc@trening.rs', 'Jovana Cvijića 10, Subotica', '0645678901'),
(6, 'Centar za razvoj terapeutskih veština', 'crt@obuka.rs', 'Trg slobode 8, Čačak', '0656789012'),
(7, 'Škola za integrativnu psihoterapiju', 'sip@skola.rs', 'Železnička 5, Zrenjanin', '0667890123'),
(8, 'Nacionalni centar za psihoterapiju', 'ncp@psihoterapija.rs', 'Obilićeva 17, Pančevo', '0608901234'),
(9, 'Centar za humanističku psihoterapiju', 'chp@humanistika.rs', 'Dositejeva 22, Sombor', '0619012345'),
(10, 'Akademija mindfulness psihoterapije', 'mindfulness@akademija.rs', 'Vojvode Stepe 55, Kruševac', '0620123456');

-- --------------------------------------------------------

--
-- Table structure for table `fakultet`
--

CREATE TABLE `fakultet` (
  `fakultet_id` int(11) NOT NULL,
  `naziv` varchar(40) NOT NULL,
  `Univerzitet_univerzitet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fakultet`
--

INSERT INTO `fakultet` (`fakultet_id`, `naziv`, `Univerzitet_univerzitet_id`) VALUES
(12, 'Centar za psihometriju', 2),
(15, 'Centar za socijalne i bihevioralne nauke', 7),
(4, 'Fakultet eksperimentalne psihologije', 4),
(6, 'Fakultet obrazovnih nauka', 6),
(3, 'Fakultet psihijatrije', 3),
(9, 'Fakultet za neuropsihologiju', 9),
(14, 'Fakultet za psihoterapiju', 5),
(11, 'Fakultet za razvojnu psihologiju', 1),
(13, 'Institut za pedagošku psihologiju', 4),
(5, 'Odeljenje za kliničku psihologiju', 5),
(2, 'Odeljenje za kognitivne nauke', 2),
(10, 'Odeljenje za kognitivne nauke', 10),
(1, 'Odeljenje za psihologiju', 1),
(8, 'Odeljenje za psihologiju', 8),
(7, 'Odeljenje za socijalnu psihologiju', 7);

-- --------------------------------------------------------

--
-- Table structure for table `fakultet_oblast`
--

CREATE TABLE `fakultet_oblast` (
  `Fakultet_fakultet_id` int(11) NOT NULL,
  `Oblast_oblast_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fakultet_oblast`
--

INSERT INTO `fakultet_oblast` (`Fakultet_fakultet_id`, `Oblast_oblast_id`) VALUES
(1, 4),
(1, 5),
(2, 1),
(3, 2),
(4, 4),
(5, 10),
(6, 6),
(7, 7),
(7, 10),
(8, 9),
(9, 9),
(10, 1),
(11, 3),
(12, 8),
(13, 6),
(14, 10),
(15, 7);

-- --------------------------------------------------------

--
-- Table structure for table `istorija_promene`
--

CREATE TABLE `istorija_promene` (
  `istorija_promene_id` int(11) NOT NULL,
  `stari_kuras` float(5,2) NOT NULL,
  `novi_kurs` float(5,2) NOT NULL,
  `datum_promene` date NOT NULL,
  `Valuta_valuta_id` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `istorija_promene`
--

INSERT INTO `istorija_promene` (`istorija_promene_id`, `stari_kuras`, `novi_kurs`, `datum_promene`, `Valuta_valuta_id`) VALUES
(1, 100.50, 102.75, '2022-05-12', 'USD'),
(2, 102.75, 103.20, '2023-03-08', 'USD'),
(3, 117.00, 118.30, '2021-10-15', 'EUR'),
(4, 118.30, 119.10, '2022-12-05', 'EUR'),
(5, 135.60, 134.45, '2022-01-25', 'GBP'),
(6, 110.00, 111.20, '2023-06-20', 'CHF'),
(7, 111.20, 112.00, '2024-02-14', 'CHF'),
(8, 1.00, 1.00, '2020-01-01', 'RSD'),
(9, 0.85, 0.87, '2023-07-18', 'JPY'),
(10, 75.00, 76.50, '2021-04-23', 'CAD'),
(11, 76.50, 77.80, '2022-08-09', 'CAD'),
(12, 80.20, 81.10, '2020-11-11', 'AUD'),
(13, 6.40, 6.50, '2022-03-14', 'CNY'),
(14, 6.50, 6.30, '2023-05-27', 'CNY'),
(15, 10.00, 10.25, '2021-09-02', 'SEK');

-- --------------------------------------------------------

--
-- Table structure for table `izrada_testa`
--

CREATE TABLE `izrada_testa` (
  `izrada_testa_id` int(11) NOT NULL,
  `rezultat` int(11) NOT NULL,
  `Seansa_seansa_id` int(11) NOT NULL,
  `Psiholoski_test_psiholoski_test_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `izrada_testa`
--

INSERT INTO `izrada_testa` (`izrada_testa_id`, `rezultat`, `Seansa_seansa_id`, `Psiholoski_test_psiholoski_test_id`) VALUES
(1, 102, 1, 1),
(2, 115, 2, 2),
(3, 88, 3, 3),
(4, 78, 4, 4),
(5, 85, 5, 5),
(6, 110, 6, 6),
(7, 40, 7, 7),
(8, 90, 8, 8),
(9, 105, 9, 9),
(10, 92, 10, 10);

-- --------------------------------------------------------

--
-- Table structure for table `kandidat`
--

CREATE TABLE `kandidat` (
  `JMBG` varchar(13) NOT NULL,
  `ime` varchar(15) NOT NULL,
  `prezime` varchar(15) NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `broj_telefona` varchar(13) NOT NULL,
  `prebivaliste` varchar(20) NOT NULL,
  `Centar_za_obuku_centar_id` int(11) NOT NULL,
  `Psihoterapeut_psihoterapeut_id` int(11) NOT NULL,
  `Stepen_studija_stepen_studija_id` int(11) NOT NULL,
  `Fakultet_fakultet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kandidat`
--

INSERT INTO `kandidat` (`JMBG`, `ime`, `prezime`, `datum_rodjenja`, `broj_telefona`, `prebivaliste`, `Centar_za_obuku_centar_id`, `Psihoterapeut_psihoterapeut_id`, `Stepen_studija_stepen_studija_id`, `Fakultet_fakultet_id`) VALUES
('0101990450012', 'Marija', 'Jovanović', '1995-04-01', '0601234567', 'Beograd', 1, 1, 2, 3),
('0202980500015', 'Nikola', 'Petrović', '1996-08-02', '0612345678', 'Novi Sad', 2, 2, 1, 5),
('0503973400021', 'Jelena', 'Marković', '1997-03-05', '0623456789', 'Niš', 3, 3, 2, 7),
('0509948200020', 'Filip', 'Nikolić', '1999-09-05', '0634445566', 'Loznica', 4, 2, 2, 14),
('0808983200026', 'Miloš', 'Pavlović', '1998-08-08', '0678901234', 'Pančevo', 8, 8, 3, 1),
('1108957400009', 'Stefan', 'Ilić', '1995-08-11', '0634567890', 'Kragujevac', 4, 4, 3, 2),
('1207965400018', 'Teodora', 'Kovačević', '1996-07-12', '0689012345', 'Sombor', 9, 9, 2, 9),
('1607996400010', 'Ana', 'Milinković', '1998-07-16', '0645678901', 'Subotica', 5, 5, 1, 4),
('1704971200011', 'Katarina', 'Petrović', '1997-04-17', '0601112233', 'Kruševac', 2, 5, 2, 11),
('1906956400013', 'Nemanja', 'Janković', '1995-06-19', '0612223344', 'Smederevo', 1, 3, 3, 12),
('2208957300024', 'Sara', 'Simić', '1996-08-22', '0623334455', 'Vranje', 3, 4, 1, 13),
('2309957400025', 'Vladimir', 'Đorđević', '1997-09-23', '0690123456', 'Šabac', 10, 10, 1, 10),
('2404978300022', 'Marko', 'Stojanović', '1994-04-24', '0656789012', 'Čačak', 6, 6, 2, 6),
('2908961200004', 'Ivana', 'Lazić', '1999-08-29', '0667890123', 'Zrenjanin', 7, 7, 1, 8),
('3101959200028', 'Milica', 'Vasić', '1995-01-31', '0645556677', 'Užice', 5, 1, 3, 15);

-- --------------------------------------------------------

--
-- Table structure for table `klijent`
--

CREATE TABLE `klijent` (
  `klijent_id` int(11) NOT NULL,
  `ime` varchar(15) NOT NULL,
  `prezime` varchar(15) NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `pol` varchar(5) NOT NULL,
  `email` varchar(35) NOT NULL,
  `broj_telefona` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `klijent`
--

INSERT INTO `klijent` (`klijent_id`, `ime`, `prezime`, `datum_rodjenja`, `pol`, `email`, `broj_telefona`) VALUES
(1, 'Jovan', 'Marković', '1995-03-21', 'M', 'jovan.markovic@gmail.com', '0612345678'),
(2, 'Marija', 'Stojanović', '1988-11-14', 'Ž', 'marija.stojanovic@yahoo.com', '0623456789'),
(3, 'Nikola', 'Ristić', '1992-07-08', 'M', 'nikola.ristic@outlook.com', '0634567890'),
(4, 'Ivana', 'Kovačević', '1997-02-25', 'Ž', 'ivana.kovacevic@gmail.com', '0645678901'),
(5, 'Milan', 'Popović', '1990-09-30', 'M', 'milan.popovic@yahoo.com', '0656789012'),
(6, 'Teodora', 'Petrović', '1985-12-17', 'Ž', 'teodora.petrovic@outlook.com', '0667890123'),
(7, 'Stefan', 'Janković', '1999-05-11', 'M', 'stefan.jankovic@gmail.com', '0608901234'),
(8, 'Jelena', 'Milovanović', '1993-08-05', 'Ž', 'jelena.milovanovic@yahoo.com', '0619012345'),
(9, 'Vladimir', 'Savić', '1987-04-22', 'M', 'vladimir.savic@outlook.com', '0620123456'),
(10, 'Katarina', 'Ilić', '1996-06-19', 'Ž', 'katarina.ilic@gmail.com', '0631234567');

-- --------------------------------------------------------

--
-- Table structure for table `objavljivanje_podataka`
--

CREATE TABLE `objavljivanje_podataka` (
  `objavljivanje_podataka_id` int(11) NOT NULL,
  `datum_objavljivanja` date NOT NULL,
  `kome_je_objavljeno` varchar(60) NOT NULL,
  `Vrsta_objavljivanja_vrsta_objavljivanja_id` int(11) NOT NULL,
  `Seansa_seansa_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `objavljivanje_podataka`
--

INSERT INTO `objavljivanje_podataka` (`objavljivanje_podataka_id`, `datum_objavljivanja`, `kome_je_objavljeno`, `Vrsta_objavljivanja_vrsta_objavljivanja_id`, `Seansa_seansa_id`) VALUES
(1, '2021-05-12', 'Sudija: Marko Petrović', 1, 1),
(2, '2023-02-18', 'Policijska uprava: PU Novi Sad', 2, 2),
(3, '2022-09-23', 'Sudija: Ivana Jovanović', 1, 3),
(4, '2020-11-05', 'Policijska uprava: PU Beograd', 2, 4),
(5, '2021-07-30', 'Sudija: Nenad Ilić', 1, 5),
(6, '2022-04-17', 'Policijska uprava: PU Niš', 2, 6),
(7, '2024-01-09', 'Sudija: Ana Milenković', 1, 7),
(8, '2023-06-21', 'Policijska uprava: PU Kragujevac', 2, 8),
(9, '2021-10-14', 'Sudija: Luka Stojanović', 1, 9),
(10, '2022-12-05', 'Policijska uprava: PU Subotica', 2, 10),
(11, '2020-08-29', 'Sudija: Jelena Ristić', 1, 11),
(12, '2023-03-16', 'Policijska uprava: PU Zrenjanin', 2, 12),
(13, '2021-01-24', 'Sudija: Stefan Dimitrijević', 1, 13),
(14, '2024-02-02', 'Policijska uprava: PU Pančevo', 2, 14),
(15, '2022-07-11', 'Sudija: Milica Đorđević', 1, 15);

-- --------------------------------------------------------

--
-- Table structure for table `oblast`
--

CREATE TABLE `oblast` (
  `oblast_id` int(11) NOT NULL,
  `naziv` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `oblast`
--

INSERT INTO `oblast` (`oblast_id`, `naziv`) VALUES
(6, 'Geštalt terapija'),
(5, 'Humanistička terapija'),
(10, 'Integrativna terapija'),
(1, 'Kognitivna terapija'),
(9, 'Mindfulness terapija'),
(3, 'Porodična terapija'),
(2, 'Psihodinamska terapija'),
(8, 'REBT'),
(4, 'Terapija ponašanja'),
(7, 'Transakciona analiza');

-- --------------------------------------------------------

--
-- Table structure for table `oblast_psihoterapije`
--

CREATE TABLE `oblast_psihoterapije` (
  `oblast_psihoterapije_id` int(11) NOT NULL,
  `naziv` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `oblast_psihoterapije`
--

INSERT INTO `oblast_psihoterapije` (`oblast_psihoterapije_id`, `naziv`) VALUES
(10, 'Analitička psihologija'),
(4, 'Geštalt terapija'),
(5, 'Humanistička terapija'),
(8, 'Integrativna psihoterapija'),
(1, 'Kognitivno-bihejvioralna terapija'),
(3, 'Porodična sistemska terapija'),
(2, 'Psihodinamska terapija'),
(9, 'Psihoterapija dece i adolescenata'),
(6, 'Terapija traumatskih iskustava'),
(7, 'Terapija zasnovana na svesnosti');

-- --------------------------------------------------------

--
-- Table structure for table `oblast_uze_usmerenje`
--

CREATE TABLE `oblast_uze_usmerenje` (
  `Oblast_oblast_id` int(11) NOT NULL,
  `Uze_usmerenje_uze_usmerenje_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `oblast_uze_usmerenje`
--

INSERT INTO `oblast_uze_usmerenje` (`Oblast_oblast_id`, `Uze_usmerenje_uze_usmerenje_id`) VALUES
(1, 1),
(1, 3),
(1, 8),
(2, 2),
(2, 3),
(2, 8),
(3, 3),
(3, 7),
(4, 1),
(4, 3),
(4, 8),
(5, 1),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(9, 5),
(10, 2),
(10, 3);

-- --------------------------------------------------------

--
-- Table structure for table `placanje`
--

CREATE TABLE `placanje` (
  `placanje_id` int(11) NOT NULL,
  `svrha` varchar(25) NOT NULL,
  `rata` int(11) NOT NULL,
  `nacin_placanja` varchar(20) NOT NULL,
  `iznos` decimal(7,2) NOT NULL,
  `datum_placanja` date DEFAULT current_timestamp(),
  `Valuta_valuta_id` varchar(3) NOT NULL,
  `Seansa_seansa_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `placanje`
--

INSERT INTO `placanje` (`placanje_id`, `svrha`, `rata`, `nacin_placanja`, `iznos`, `datum_placanja`, `Valuta_valuta_id`, `Seansa_seansa_id`) VALUES
(1, 'Terapija', 0, 'Gotovina', 7500.00, '2025-04-29', 'RSD', 1),
(2, 'Terapija', 0, 'Kartica', 3750.00, '2025-04-29', 'RSD', 2),
(3, 'Terapija', 0, 'Kartica', 6000.00, '2025-04-29', 'RSD', 3),
(4, 'Terapija', 0, 'Gotovina', 7000.00, '2025-04-29', 'RSD', 4),
(5, 'Terapija', 0, 'Gotovina', 2750.00, '2025-04-29', 'RSD', 5),
(6, 'Terapija', 0, 'Kartica', 5500.00, '2025-04-29', 'RSD', 6),
(7, 'Terapija', 0, 'Kartica', 4400.00, '2025-04-29', 'RSD', 7),
(8, 'Terapija', 0, 'Gotovina', 7500.00, '2025-04-29', 'RSD', 8),
(9, 'Terapija', 0, 'Kartica', 4100.00, '2025-04-29', 'RSD', 9),
(10, 'Terapija', 1, 'Gotovina', 5000.00, '2025-04-29', 'RSD', 10),
(11, 'Terapija', 2, 'Gotovina', 5000.00, '2025-04-29', 'RSD', 10),
(12, 'Terapija', 0, 'Kartica', 8800.00, '2025-04-27', 'RSD', 18);

-- --------------------------------------------------------

--
-- Table structure for table `prijava`
--

CREATE TABLE `prijava` (
  `prijava_id` int(11) NOT NULL,
  `posecivao_psihoterapeuta` tinyint(1) NOT NULL,
  `opis_problema` longtext NOT NULL,
  `Seansa_seansa_id` int(11) NOT NULL,
  `klijent_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prijava`
--

INSERT INTO `prijava` (`prijava_id`, `posecivao_psihoterapeuta`, `opis_problema`, `Seansa_seansa_id`, `klijent_id`) VALUES
(1, 1, 'Anksioznost prisutna više od godinu dana.', 1, 1),
(2, 0, 'Problemi sa samopouzdanjem.', 2, 2),
(3, 1, 'Depresivne epizode nakon stresa.', 3, 3),
(4, 0, 'Panični napadi u socijalnim situacijama.', 4, 4),
(5, 1, 'Problemi u porodičnoj komunikaciji.', 5, 5),
(6, 0, 'Nizak nivo motivacije za svakodnevne aktivnosti.', 6, 6),
(7, 1, 'Posttraumatski simptomi nakon nesreće.', 7, 7),
(8, 0, 'Teškoće u kontrolisanju besa.', 8, 8),
(9, 1, 'Perfekcionizam i preterana samokritičnost.', 9, 9),
(10, 0, 'Strah od neuspeha na poslu.', 10, 10),
(11, 1, 'Problemi sa donošenjem odluka.', 11, 1),
(12, 0, 'Anksioznost zbog akademskog uspeha.', 12, 2),
(13, 1, 'Problemi sa hranjenjem i telesnom slikom.', 13, 3),
(14, 0, 'Zavisnost od društvenih mreža.', 14, 4),
(15, 1, 'Izražen osećaj izolovanosti i usamljenosti.', 15, 5),
(16, 0, 'problem', 18, 3);

-- --------------------------------------------------------

--
-- Table structure for table `psiholoski_test`
--

CREATE TABLE `psiholoski_test` (
  `psiholoski_test_id` int(11) NOT NULL,
  `naziv` varchar(30) NOT NULL,
  `oblast` varchar(40) NOT NULL,
  `cena` decimal(7,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `psiholoski_test`
--

INSERT INTO `psiholoski_test` (`psiholoski_test_id`, `naziv`, `oblast`, `cena`) VALUES
(1, 'MMPI', 'Psihometrija', 3000.00),
(2, 'WISC-IV', 'Kognitivne nauke', 3500.00),
(3, 'Raven', 'Psihometrija', 2000.00),
(4, 'TAT', 'Psihoterapija', 2500.00),
(5, 'Beck', 'Psihopatologija', 4000.00),
(6, 'Binet-Simon', 'Pedagoška psihologija', 1500.00),
(7, 'Kernberg', 'Psihodinamska terapija', 3000.00),
(8, 'SCL-90', 'Klinicka psihologija', 5000.00),
(9, 'WAIS', 'Kognitivne nauke', 4500.00),
(10, 'Rorschach', 'Psihoterapija', 1800.00);

-- --------------------------------------------------------

--
-- Table structure for table `psihoterapeut`
--

CREATE TABLE `psihoterapeut` (
  `psihoterapeut_id` int(11) NOT NULL,
  `ime` varchar(15) NOT NULL,
  `prezime` varchar(15) NOT NULL,
  `JMBG` varchar(13) NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `prebivaliste` varchar(20) NOT NULL,
  `broj_telefona` varchar(13) NOT NULL,
  `psiholog` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `psihoterapeut`
--

INSERT INTO `psihoterapeut` (`psihoterapeut_id`, `ime`, `prezime`, `JMBG`, `datum_rodjenja`, `prebivaliste`, `broj_telefona`, `psiholog`) VALUES
(1, 'Miloš', 'Petrović', '0101980123456', '1980-01-01', 'Beograd', '0612345678', 1),
(2, 'Ana', 'Jovanović', '1502199012345', '1990-02-15', 'Novi Sad', '0623456789', 1),
(3, 'Marko', 'Nikolic', '2303198512345', '1985-03-23', 'Nis', '0634567892', 0),
(4, 'Ivana', 'Simić', '0704199312345', '1993-04-07', 'Kragujevac', '0645678901', 1),
(5, 'Stefan', 'Lukić', '1205199212345', '1992-05-12', 'Subotica', '0656789012', 1),
(6, 'Jelena', 'Milinković', '2506198912345', '1989-06-25', 'Zrenjanin', '0667890123', 0),
(7, 'Aleksandar', 'Đorđević', '3012198412345', '1984-12-30', 'Čačak', '0608901234', 1),
(8, 'Milica', 'Stanković', '0807199512345', '1995-07-08', 'Pančevo', '0619012345', 1),
(9, 'Vladimir', 'Vasić', '1610198712345', '1987-10-16', 'Sombor', '0620123456', 0),
(10, 'Katarina', 'Matić', '2208199812345', '1998-08-22', 'Kruševac', '0631234567', 1);

-- --------------------------------------------------------

--
-- Table structure for table `seansa`
--

CREATE TABLE `seansa` (
  `seansa_id` int(11) NOT NULL,
  `dan_vreme` datetime NOT NULL,
  `trajanje_minuti` int(11) NOT NULL,
  `beleske` longtext DEFAULT NULL,
  `Kandidat_JMBG` varchar(13) DEFAULT NULL,
  `Psihoterapeut_psihoterapeut_id` int(11) DEFAULT NULL,
  `Cena_cena_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seansa`
--

INSERT INTO `seansa` (`seansa_id`, `dan_vreme`, `trajanje_minuti`, `beleske`, `Kandidat_JMBG`, `Psihoterapeut_psihoterapeut_id`, `Cena_cena_id`) VALUES
(1, '2025-04-01 10:00:00', 60, 'Prvi susret, procena motivacije.', '0101990450012', 1, 1),
(2, '2025-04-03 12:00:00', 45, 'Razrada ciljeva terapije.', '0202980500015', 2, 2),
(3, '2025-04-05 14:00:00', 60, 'Rad na kognitivnim distorzijama.', '0503973400021', 3, 3),
(4, '2025-04-06 16:30:00', 90, 'Psihodinamska interpretacija snova.', '1108957400009', 4, 4),
(5, '2025-04-07 09:00:00', 30, 'Uvodni intervju.', '1607996400010', 5, 5),
(6, '2025-04-08 11:00:00', 60, 'Porodične relacije - genogram.', '2404978300022', 6, 6),
(7, '2025-04-10 13:00:00', 45, 'Terapija ponašanja - zadaci.', '2908961200004', 7, 7),
(8, '2025-04-12 15:00:00', 60, 'Humanistički pristup - osnaživanje.', '0808983200026', 8, 8),
(9, '2025-04-14 17:00:00', 75, 'Geštalt tehnike - prazna stolica.', '1207965400018', 9, 9),
(10, '2025-04-16 09:30:00', 60, 'Mindfulness trening.', '2309957400025', 10, 10),
(11, '2025-04-18 11:00:00', 60, 'Rad na emocionalnoj regulaciji.', '2208957300024', 1, 1),
(12, '2025-04-19 13:30:00', 90, 'Kognitivno-bihevioralne vežbe.', '2309957400025', 2, 2),
(13, '2025-04-20 15:00:00', 45, 'Vežbe samopouzdanja.', '2404978300022', 3, 3),
(14, '2025-04-21 17:00:00', 60, 'Transakciona analiza - ego stanja.', '2908961200004', 4, 4),
(15, '2025-04-22 09:30:00', 75, 'Završna procena i povratne informacije.', '3101959200028', 5, 5),
(16, '2025-04-28 21:00:27', 45, NULL, '0101990450012', 3, 1),
(17, '2025-05-05 18:06:48', 45, NULL, '0101990450012', 2, 5),
(18, '2025-04-28 19:49:53', 30, 'PTSD', '0202980500015', 3, 7),
(19, '2025-06-28 19:49:55', 60, NULL, NULL, 3, 10);

-- --------------------------------------------------------

--
-- Table structure for table `sertifikat`
--

CREATE TABLE `sertifikat` (
  `sertifikat_id` int(11) NOT NULL,
  `datum_izrade` date NOT NULL,
  `Psihoterapeut_psihoterapeut_id` int(11) NOT NULL,
  `Oblast_psihoterapije_oblast_psihoterapije_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sertifikat`
--

INSERT INTO `sertifikat` (`sertifikat_id`, `datum_izrade`, `Psihoterapeut_psihoterapeut_id`, `Oblast_psihoterapije_oblast_psihoterapije_id`) VALUES
(1, '2023-06-12', 1, 1),
(2, '2022-11-05', 2, 2),
(3, '2023-03-18', 3, 3),
(4, '2021-09-27', 4, 4),
(5, '2024-01-15', 5, 5),
(6, '2022-05-30', 6, 6),
(7, '2023-02-11', 7, 7),
(8, '2022-08-19', 8, 8),
(9, '2023-10-22', 9, 9),
(10, '2024-03-09', 10, 10);

-- --------------------------------------------------------

--
-- Table structure for table `stepen_studija`
--

CREATE TABLE `stepen_studija` (
  `stepen_studija_id` int(11) NOT NULL,
  `naziv` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stepen_studija`
--

INSERT INTO `stepen_studija` (`stepen_studija_id`, `naziv`) VALUES
(3, 'Doktorske studi'),
(2, 'Master studije'),
(1, 'Osnovne studije');

-- --------------------------------------------------------

--
-- Table structure for table `univerzitet`
--

CREATE TABLE `univerzitet` (
  `univerzitet_id` int(11) NOT NULL,
  `naziv` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `univerzitet`
--

INSERT INTO `univerzitet` (`univerzitet_id`, `naziv`) VALUES
(1, 'Univerzitet Harvard'),
(8, 'Univerzitet Jejl'),
(10, 'Univerzitet Kalifornije'),
(3, 'Univerzitet Kembridž'),
(4, 'Univerzitet Oksford'),
(2, 'Univerzitet Stanford'),
(7, 'Univerzitet u Amsterdamu'),
(9, 'Univerzitet u Edinburgu'),
(6, 'Univerzitet u Melburnu'),
(5, 'Univerzitet u Torontu');

-- --------------------------------------------------------

--
-- Table structure for table `uze_usmerenje`
--

CREATE TABLE `uze_usmerenje` (
  `uze_usmerenje_id` int(11) NOT NULL,
  `naziv` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `uze_usmerenje`
--

INSERT INTO `uze_usmerenje` (`uze_usmerenje_id`, `naziv`) VALUES
(8, 'Klinicka psihologija'),
(4, 'Kognitivne nauke'),
(9, 'Neuropsihologija'),
(6, 'Pedagoška psihologija'),
(2, 'Psihijatrija'),
(1, 'Psihologija'),
(10, 'Psihometrija'),
(3, 'Psihoterapija'),
(7, 'Razvojna psihologija'),
(5, 'Socijalna psihologija');

-- --------------------------------------------------------

--
-- Table structure for table `uze_usmerenje_univerzitet`
--

CREATE TABLE `uze_usmerenje_univerzitet` (
  `Uze_usmerenje_uze_usmerenje_id` int(11) NOT NULL,
  `Univerzitet_univerzitet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `uze_usmerenje_univerzitet`
--

INSERT INTO `uze_usmerenje_univerzitet` (`Uze_usmerenje_uze_usmerenje_id`, `Univerzitet_univerzitet_id`) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 2),
(4, 3),
(5, 3),
(5, 4),
(6, 6),
(7, 5),
(8, 8),
(9, 7),
(9, 10),
(10, 10);

-- --------------------------------------------------------

--
-- Table structure for table `valuta`
--

CREATE TABLE `valuta` (
  `valuta_id` varchar(3) NOT NULL,
  `pun_naziv` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `valuta`
--

INSERT INTO `valuta` (`valuta_id`, `pun_naziv`) VALUES
('USD', 'American Dollar'),
('AUD', 'Australian Dollar'),
('GBP', 'British Pound'),
('CAD', 'Canadian Dollar'),
('CNY', 'Chinese Yuan'),
('EUR', 'Euro'),
('JPY', 'Japanese Yen'),
('RSD', 'Serbian Dinar'),
('SEK', 'Swedish Krona'),
('CHF', 'Swiss Franc');

-- --------------------------------------------------------

--
-- Table structure for table `vrsta_objavljivanja`
--

CREATE TABLE `vrsta_objavljivanja` (
  `vrsta_objavljivanja_id` int(11) NOT NULL,
  `naziv` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vrsta_objavljivanja`
--

INSERT INTO `vrsta_objavljivanja` (`vrsta_objavljivanja_id`, `naziv`) VALUES
(2, 'Objavljivanje policiji'),
(1, 'Objavljivanje sudu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cena`
--
ALTER TABLE `cena`
  ADD PRIMARY KEY (`cena_id`),
  ADD KEY `idx_cena_datum_tr_cena` (`datum_promene`,`trenutna_cena`);

--
-- Indexes for table `centar_za_obuku`
--
ALTER TABLE `centar_za_obuku`
  ADD PRIMARY KEY (`centar_id`),
  ADD UNIQUE KEY `uq_centar_email` (`email`),
  ADD UNIQUE KEY `uq_centar_broj_telefona` (`broj_telefona`),
  ADD KEY `idx_centar_naziv` (`naziv`);

--
-- Indexes for table `fakultet`
--
ALTER TABLE `fakultet`
  ADD PRIMARY KEY (`fakultet_id`),
  ADD UNIQUE KEY `uq_fakultet_naziv_univerzitet` (`naziv`,`Univerzitet_univerzitet_id`),
  ADD KEY `Fakultet_Univerzitet` (`Univerzitet_univerzitet_id`);

--
-- Indexes for table `fakultet_oblast`
--
ALTER TABLE `fakultet_oblast`
  ADD PRIMARY KEY (`Fakultet_fakultet_id`,`Oblast_oblast_id`),
  ADD KEY `Fakultet_Oblast_Oblast` (`Oblast_oblast_id`);

--
-- Indexes for table `istorija_promene`
--
ALTER TABLE `istorija_promene`
  ADD PRIMARY KEY (`istorija_promene_id`),
  ADD KEY `Istorija_promene_Valuta` (`Valuta_valuta_id`),
  ADD KEY `idx_istorija_promene_datum_promene` (`datum_promene`),
  ADD KEY `idx_istorija_promene_stari_novi_kurs` (`stari_kuras`,`novi_kurs`);

--
-- Indexes for table `izrada_testa`
--
ALTER TABLE `izrada_testa`
  ADD PRIMARY KEY (`izrada_testa_id`,`Psiholoski_test_psiholoski_test_id`,`Seansa_seansa_id`),
  ADD KEY `Izrada_testa_Psiholoski_test` (`Psiholoski_test_psiholoski_test_id`),
  ADD KEY `Izrada_testa_Seansa` (`Seansa_seansa_id`);

--
-- Indexes for table `kandidat`
--
ALTER TABLE `kandidat`
  ADD PRIMARY KEY (`JMBG`),
  ADD UNIQUE KEY `uq_broj_telefona` (`broj_telefona`),
  ADD KEY `Kandidat_Centar_za_obuku` (`Centar_za_obuku_centar_id`),
  ADD KEY `Kandidat_Fakultet` (`Fakultet_fakultet_id`),
  ADD KEY `Kandidat_Psihoterapeut` (`Psihoterapeut_psihoterapeut_id`),
  ADD KEY `Kandidat_Stepen_studija` (`Stepen_studija_stepen_studija_id`),
  ADD KEY `idx_podaci_kandidata` (`ime`,`prezime`,`prebivaliste`,`datum_rodjenja`);

--
-- Indexes for table `klijent`
--
ALTER TABLE `klijent`
  ADD PRIMARY KEY (`klijent_id`),
  ADD UNIQUE KEY `uq_klijent_email` (`email`),
  ADD UNIQUE KEY `uq_klijent_broj_telefona` (`broj_telefona`),
  ADD KEY `id_podaci_klijenta` (`ime`,`prezime`,`pol`,`datum_rodjenja`);

--
-- Indexes for table `objavljivanje_podataka`
--
ALTER TABLE `objavljivanje_podataka`
  ADD PRIMARY KEY (`objavljivanje_podataka_id`),
  ADD KEY `Objavljivanje_podataka_Seansa` (`Seansa_seansa_id`),
  ADD KEY `Vrsta_objavljivanja_Objavljivanje_podataka` (`Vrsta_objavljivanja_vrsta_objavljivanja_id`),
  ADD KEY `idx_datum_objavljivanja` (`datum_objavljivanja`);

--
-- Indexes for table `oblast`
--
ALTER TABLE `oblast`
  ADD PRIMARY KEY (`oblast_id`),
  ADD UNIQUE KEY `uq_oblast_naziv` (`naziv`);

--
-- Indexes for table `oblast_psihoterapije`
--
ALTER TABLE `oblast_psihoterapije`
  ADD PRIMARY KEY (`oblast_psihoterapije_id`),
  ADD UNIQUE KEY `uq_oblast_psihoterapije_naziv` (`naziv`);

--
-- Indexes for table `oblast_uze_usmerenje`
--
ALTER TABLE `oblast_uze_usmerenje`
  ADD PRIMARY KEY (`Oblast_oblast_id`,`Uze_usmerenje_uze_usmerenje_id`),
  ADD KEY `Oblast_Uze_usmerenje_Uze_usmerenje` (`Uze_usmerenje_uze_usmerenje_id`);

--
-- Indexes for table `placanje`
--
ALTER TABLE `placanje`
  ADD PRIMARY KEY (`placanje_id`),
  ADD KEY `Placanje_Seansa` (`Seansa_seansa_id`),
  ADD KEY `Placanje_Valuta` (`Valuta_valuta_id`),
  ADD KEY `idx_nacin_placanja_iznos` (`nacin_placanja`,`iznos`);

--
-- Indexes for table `prijava`
--
ALTER TABLE `prijava`
  ADD PRIMARY KEY (`prijava_id`),
  ADD KEY `Prijava_Klijent` (`klijent_id`),
  ADD KEY `Prijava_Seansa` (`Seansa_seansa_id`);

--
-- Indexes for table `psiholoski_test`
--
ALTER TABLE `psiholoski_test`
  ADD PRIMARY KEY (`psiholoski_test_id`),
  ADD UNIQUE KEY `uq_naziv_oblast` (`naziv`,`oblast`),
  ADD KEY `idx_cena_testa` (`cena`);

--
-- Indexes for table `psihoterapeut`
--
ALTER TABLE `psihoterapeut`
  ADD PRIMARY KEY (`psihoterapeut_id`),
  ADD UNIQUE KEY `uq_psihoterapeut_jmbg` (`JMBG`),
  ADD UNIQUE KEY `uq_psihoterapeut_broj_telefona` (`broj_telefona`),
  ADD KEY `idx_ime_prezime` (`ime`,`prezime`),
  ADD KEY `idx_psiholog` (`ime`,`prezime`,`psiholog`);

--
-- Indexes for table `seansa`
--
ALTER TABLE `seansa`
  ADD PRIMARY KEY (`seansa_id`),
  ADD KEY `Seansa_Cena` (`Cena_cena_id`),
  ADD KEY `Seansa_Kandidat` (`Kandidat_JMBG`),
  ADD KEY `Seansa_Psihoterapeut` (`Psihoterapeut_psihoterapeut_id`),
  ADD KEY `idx_dan_vreme` (`dan_vreme`),
  ADD KEY `idx_seansa_trajanje_u_minutima` (`trajanje_minuti`),
  ADD KEY `idx_seansa_beleske` (`beleske`(768));

--
-- Indexes for table `sertifikat`
--
ALTER TABLE `sertifikat`
  ADD PRIMARY KEY (`sertifikat_id`),
  ADD KEY `Sertifikat_Oblast_psihoterapije` (`Oblast_psihoterapije_oblast_psihoterapije_id`),
  ADD KEY `Sertifikat_Psihoterapeut` (`Psihoterapeut_psihoterapeut_id`),
  ADD KEY `idx_sertifikat_datum_izrade` (`datum_izrade`);

--
-- Indexes for table `stepen_studija`
--
ALTER TABLE `stepen_studija`
  ADD PRIMARY KEY (`stepen_studija_id`),
  ADD UNIQUE KEY `uq_stepen_studija_naziv` (`naziv`);

--
-- Indexes for table `univerzitet`
--
ALTER TABLE `univerzitet`
  ADD PRIMARY KEY (`univerzitet_id`),
  ADD UNIQUE KEY `uq_univerzitet_naziv` (`naziv`);

--
-- Indexes for table `uze_usmerenje`
--
ALTER TABLE `uze_usmerenje`
  ADD PRIMARY KEY (`uze_usmerenje_id`),
  ADD UNIQUE KEY `uq_uze_usmerenje_naziv` (`naziv`);

--
-- Indexes for table `uze_usmerenje_univerzitet`
--
ALTER TABLE `uze_usmerenje_univerzitet`
  ADD PRIMARY KEY (`Uze_usmerenje_uze_usmerenje_id`,`Univerzitet_univerzitet_id`),
  ADD KEY `Uze_usmerenje_Univerzitet_Univerzitet` (`Univerzitet_univerzitet_id`);

--
-- Indexes for table `valuta`
--
ALTER TABLE `valuta`
  ADD PRIMARY KEY (`valuta_id`),
  ADD UNIQUE KEY `uq_valuta_pun_naziv` (`pun_naziv`);

--
-- Indexes for table `vrsta_objavljivanja`
--
ALTER TABLE `vrsta_objavljivanja`
  ADD PRIMARY KEY (`vrsta_objavljivanja_id`),
  ADD UNIQUE KEY `uq_vrsta_objavljivanja_naziv` (`naziv`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `objavljivanje_podataka`
--
ALTER TABLE `objavljivanje_podataka`
  MODIFY `objavljivanje_podataka_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `psihoterapeut`
--
ALTER TABLE `psihoterapeut`
  MODIFY `psihoterapeut_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `vrsta_objavljivanja`
--
ALTER TABLE `vrsta_objavljivanja`
  MODIFY `vrsta_objavljivanja_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `fakultet`
--
ALTER TABLE `fakultet`
  ADD CONSTRAINT `Fakultet_Univerzitet` FOREIGN KEY (`Univerzitet_univerzitet_id`) REFERENCES `univerzitet` (`univerzitet_id`);

--
-- Constraints for table `fakultet_oblast`
--
ALTER TABLE `fakultet_oblast`
  ADD CONSTRAINT `Fakultet_Oblast_Fakultet` FOREIGN KEY (`Fakultet_fakultet_id`) REFERENCES `fakultet` (`fakultet_id`),
  ADD CONSTRAINT `Fakultet_Oblast_Oblast` FOREIGN KEY (`Oblast_oblast_id`) REFERENCES `oblast` (`oblast_id`);

--
-- Constraints for table `istorija_promene`
--
ALTER TABLE `istorija_promene`
  ADD CONSTRAINT `Istorija_promene_Valuta` FOREIGN KEY (`Valuta_valuta_id`) REFERENCES `valuta` (`valuta_id`);

--
-- Constraints for table `izrada_testa`
--
ALTER TABLE `izrada_testa`
  ADD CONSTRAINT `Izrada_testa_Psiholoski_test` FOREIGN KEY (`Psiholoski_test_psiholoski_test_id`) REFERENCES `psiholoski_test` (`psiholoski_test_id`),
  ADD CONSTRAINT `Izrada_testa_Seansa` FOREIGN KEY (`Seansa_seansa_id`) REFERENCES `seansa` (`seansa_id`);

--
-- Constraints for table `kandidat`
--
ALTER TABLE `kandidat`
  ADD CONSTRAINT `Kandidat_Centar_za_obuku` FOREIGN KEY (`Centar_za_obuku_centar_id`) REFERENCES `centar_za_obuku` (`centar_id`),
  ADD CONSTRAINT `Kandidat_Fakultet` FOREIGN KEY (`Fakultet_fakultet_id`) REFERENCES `fakultet` (`fakultet_id`),
  ADD CONSTRAINT `Kandidat_Psihoterapeut` FOREIGN KEY (`Psihoterapeut_psihoterapeut_id`) REFERENCES `psihoterapeut` (`psihoterapeut_id`),
  ADD CONSTRAINT `Kandidat_Stepen_studija` FOREIGN KEY (`Stepen_studija_stepen_studija_id`) REFERENCES `stepen_studija` (`stepen_studija_id`);

--
-- Constraints for table `objavljivanje_podataka`
--
ALTER TABLE `objavljivanje_podataka`
  ADD CONSTRAINT `Objavljivanje_podataka_Seansa` FOREIGN KEY (`Seansa_seansa_id`) REFERENCES `seansa` (`seansa_id`),
  ADD CONSTRAINT `Vrsta_objavljivanja_Objavljivanje_podataka` FOREIGN KEY (`Vrsta_objavljivanja_vrsta_objavljivanja_id`) REFERENCES `vrsta_objavljivanja` (`vrsta_objavljivanja_id`);

--
-- Constraints for table `oblast_uze_usmerenje`
--
ALTER TABLE `oblast_uze_usmerenje`
  ADD CONSTRAINT `Oblast_Uze_usmerenje_Oblast` FOREIGN KEY (`Oblast_oblast_id`) REFERENCES `oblast` (`oblast_id`),
  ADD CONSTRAINT `Oblast_Uze_usmerenje_Uze_usmerenje` FOREIGN KEY (`Uze_usmerenje_uze_usmerenje_id`) REFERENCES `uze_usmerenje` (`uze_usmerenje_id`);

--
-- Constraints for table `placanje`
--
ALTER TABLE `placanje`
  ADD CONSTRAINT `Placanje_Seansa` FOREIGN KEY (`Seansa_seansa_id`) REFERENCES `seansa` (`seansa_id`),
  ADD CONSTRAINT `Placanje_Valuta` FOREIGN KEY (`Valuta_valuta_id`) REFERENCES `valuta` (`valuta_id`);

--
-- Constraints for table `prijava`
--
ALTER TABLE `prijava`
  ADD CONSTRAINT `Prijava_Klijent` FOREIGN KEY (`klijent_id`) REFERENCES `klijent` (`klijent_id`),
  ADD CONSTRAINT `Prijava_Seansa` FOREIGN KEY (`Seansa_seansa_id`) REFERENCES `seansa` (`seansa_id`);

--
-- Constraints for table `seansa`
--
ALTER TABLE `seansa`
  ADD CONSTRAINT `Seansa_Cena` FOREIGN KEY (`Cena_cena_id`) REFERENCES `cena` (`cena_id`),
  ADD CONSTRAINT `Seansa_Kandidat` FOREIGN KEY (`Kandidat_JMBG`) REFERENCES `kandidat` (`JMBG`),
  ADD CONSTRAINT `Seansa_Psihoterapeut` FOREIGN KEY (`Psihoterapeut_psihoterapeut_id`) REFERENCES `psihoterapeut` (`psihoterapeut_id`);

--
-- Constraints for table `sertifikat`
--
ALTER TABLE `sertifikat`
  ADD CONSTRAINT `Sertifikat_Oblast_psihoterapije` FOREIGN KEY (`Oblast_psihoterapije_oblast_psihoterapije_id`) REFERENCES `oblast_psihoterapije` (`oblast_psihoterapije_id`),
  ADD CONSTRAINT `Sertifikat_Psihoterapeut` FOREIGN KEY (`Psihoterapeut_psihoterapeut_id`) REFERENCES `psihoterapeut` (`psihoterapeut_id`);

--
-- Constraints for table `uze_usmerenje_univerzitet`
--
ALTER TABLE `uze_usmerenje_univerzitet`
  ADD CONSTRAINT `Uze_usmerenje_Univerzitet_Univerzitet` FOREIGN KEY (`Univerzitet_univerzitet_id`) REFERENCES `univerzitet` (`univerzitet_id`),
  ADD CONSTRAINT `Uze_usmerenje_Univerzitet_Uze_usmerenje` FOREIGN KEY (`Uze_usmerenje_uze_usmerenje_id`) REFERENCES `uze_usmerenje` (`uze_usmerenje_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
