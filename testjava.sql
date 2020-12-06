-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 12, 2018 at 05:26 PM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `testjava`
--

-- --------------------------------------------------------

--
-- Table structure for table `info_user`
--

CREATE TABLE `info_user` (
  `ID` int(11) NOT NULL,
  `user_name` text COLLATE utf8_vietnamese_ci NOT NULL,
  `pass` text COLLATE utf8_vietnamese_ci NOT NULL,
  `fullname` text COLLATE utf8_vietnamese_ci,
  `totalCore` double NOT NULL,
  `totalGames` int(11) NOT NULL,
  `totalTimePlayed` double NOT NULL,
  `totalWins` int(11) NOT NULL,
  `isOnline` int(11) NOT NULL,
  `status` varchar(50) COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `info_user`
--

INSERT INTO `info_user` (`ID`, `user_name`, `pass`, `fullname`, `totalCore`, `totalGames`, `totalTimePlayed`, `totalWins`, `isOnline`, `status`) VALUES
(1, 'a', '1', 'Dương Đình Tân', 1, 1, 10, 1, 0, 'Bạn có dự định gì'),
(2, 'b', '1', 'full name b', 1, 9, 3, 1, 0, ''),
(3, 'c', '1', 'full name c', 0, 0, 0, 0, 0, ''),
(4, 'd', '', 'full name d', 0, 0, 0, 0, 0, ''),
(5, 'e', '1', 'full name e', 0, 0, 0, 0, 0, ''),
(6, 'f', '1', 'full name f', 0, 0, 0, 0, 0, ''),
(7, 'tantinhte01', '1', 'duong tan 01', 0, 0, 0, 0, 0, ''),
(8, 'tantinhte02', '1', 'duong tan 02', 0, 0, 0, 0, 0, ''),
(9, 'tantinhte03', '1', 'duong tan 03', 0, 0, 0, 0, 0, ''),
(10, 'tantinhte01', '1', 'duong tan 01', 0, 0, 0, 0, 0, ''),
(11, 'tantinhte02', '1', 'duong tan 02', 0, 0, 0, 0, 0, ''),
(12, 'tantinhte03', '1', 'duong tan 03', 0, 0, 0, 0, 0, ''),
(13, 'tantinhte01', '1', 'duong tan 01', 0, 0, 0, 0, 0, ''),
(14, 'tantinhte02', '1', 'duong tan 02', 0, 0, 0, 0, 0, ''),
(15, 'tantinhte03', '1', 'duong tan 03', 0, 0, 0, 0, 0, ''),
(16, 'p', 'p', 'p', 0, 0, 0, 0, 0, ''),
(17, 'z', '1', 'my name is z', 0, 0, 0, 0, 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `relationship`
--

CREATE TABLE `relationship` (
  `id1` int(11) NOT NULL,
  `id2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `relationship`
--

INSERT INTO `relationship` (`id1`, `id2`) VALUES
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(17, 1),
(2, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `info_user`
--
ALTER TABLE `info_user`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `info_user`
--
ALTER TABLE `info_user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
