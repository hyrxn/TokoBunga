-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 26, 2023 at 11:05 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tokobunga_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `productID` char(5) NOT NULL,
  `addedBy` char(5) NOT NULL,
  `productName` varchar(40) NOT NULL,
  `color` varchar(200) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `is_delete` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productID`, `addedBy`, `productName`, `color`, `price`, `quantity`, `is_delete`) VALUES
('PR001', 'US005', 'Lily', 'White', 12000, 15, 1),
('PR003', 'admin', 'Rose', 'Pink', 15000, 10, 1),
('PR004', 'US006', 'Sunflower', 'Yellow', 23000, 20, 1),
('PR005', 'US006', 'Chrysanthemum', 'Yellow', 9000, 10, 0),
('PR006', 'US006', 'Water Lily', 'White', 10000, 4, 0),
('PR007', 'US005', 'Tulip', 'Violet', 15000, 13, 0),
('PR008', 'US005', 'Marigold', 'Orange', 20000, 10, 0),
('PR009', 'US005', 'Orchid', 'Purple', 12000, 10, 0),
('PR010', 'US006', 'Periwinkle', 'Light Pink', 10000, 7, 0),
('PR011', 'admin', 'Daisy', 'Orange', 10000, 5, 0),
('PR012', 'US005', 'Plumeria', 'Light Yellow', 15000, 0, 0),
('PR013', 'US005', 'Lotus', 'Pink', 15000, 5, 0),
('PR014', 'US005', 'Rose', 'Pink', 20000, 20, 1),
('PR015', 'US005', 'Cherry Blossom', 'Pink', 7000, 25, 0),
('PR016', 'US006', 'Rose', 'White', 20000, 20, 1),
('PR017', 'US006', 'Cosmos', 'Pink', 20000, 10, 0);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `transactionID` char(5) NOT NULL,
  `userID` char(5) NOT NULL,
  `transactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`transactionID`, `userID`, `transactionDate`) VALUES
('TR001', 'US015', '2023-01-15'),
('TR002', 'US015', '2023-01-17'),
('TR003', 'US015', '2023-01-17'),
('TR004', 'US015', '2023-01-17'),
('TR005', 'US016', '2023-01-17'),
('TR006', 'US016', '2023-01-25');

-- --------------------------------------------------------

--
-- Table structure for table `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `transactionID` char(5) NOT NULL,
  `productID` char(5) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactiondetail`
--

INSERT INTO `transactiondetail` (`transactionID`, `productID`, `quantity`) VALUES
('TR001', 'PR003', 3),
('TR002', 'PR012', 15),
('TR003', 'PR007', 17),
('TR004', 'PR006', 1),
('TR005', 'PR010', 3),
('TR006', 'PR007', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` char(5) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phoneNumber` varchar(13) NOT NULL,
  `age` int(11) NOT NULL,
  `role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `username`, `password`, `gender`, `email`, `phoneNumber`, `age`, `role`) VALUES
('US001', 'admin', 'admin', 'Male', 'admin@gmail.com', '08132567891', 21, 'admin'),
('US005', 'Gerard', 'gerard123', 'Male', 'gerard@gmail.com', '089765123298', 20, 'staff'),
('US006', 'Tabitha', 'tabitha123', 'Female', 'tabitha@gmail.com', '089508129312', 25, 'staff'),
('US015', 'cust1', 'cust123', 'Male', 'cust1@gmail.com', '0897123834123', 21, 'customer'),
('US016', 'dea', 'dea123', 'Female', 'dea@gmail.com', '0891237182', 23, 'customer'),
('US017', 'tania', 'tania123', 'Female', 'tania@gmail.com', '089128912343', 21, 'customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`productID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD PRIMARY KEY (`transactionID`,`productID`),
  ADD KEY `productID` (`productID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
