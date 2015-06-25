use master
go 
drop database E2W
go
create database E2W
go
use E2W
go 
create table Users
(
U_ID int identity(1,1) primary key,
U_username varchar(50),
U_password varchar(50),
U_name nvarchar(50),
U_Sex bit,
U_Address nvarchar(max),
U_Email varchar(100),
U_Phonenumber int,
U_Role int
)
go
create table PackageTour
(
P_ID int identity(1,1) primary key,
P_Title nvarchar(100),
P_Duration nvarchar(20),
P_Description ntext,
P_Price float,
P_Type int
)
go
create table TourType
(
T_ID int identity(1,1) primary key,
T_type nvarchar(50)
)
go
create table CarforRent
(
C_ID int identity(1,1) primary key,
C_Name varchar(50),
C_Model nvarchar(50),
C_Type nvarchar(50),
C_Seating int,
C_Airconditioner bit,
C_Price float
)
go
create table Rentcar
(
R_ID int identity(1,1) primary key,
U_ID int,
C_ID int,
R_PickUp date,
R_DropOff date,
R_Driver bit,
R_Status nvarchar(20)
)
go
create table BookingTour
(
B_ID int identity(1,1) primary key,
U_ID int,
B_Person int,
B_status nvarchar(50)
)
go
create table UserRole
(
UR_ID int identity(1,1) primary key,
UR_Name nvarchar(50)
)
go
create table placesvisit
(
V_ID int identity(1,1) primary key,
V_Title nvarchar(100),
V_Description ntext
)
Alter Table PackageTour ADD CONSTRAINT fk_tourtype Foreign Key (P_Type) REFERENCES TourType(T_ID)
Alter Table Rentcar ADD CONSTRAINT fk_UID Foreign Key (U_ID) REFERENCES Users(U_ID)
Alter Table Rentcar ADD CONSTRAINT fk_CID Foreign Key (C_ID) REFERENCES CarforRent(C_ID)
Alter Table BookingTour ADD CONSTRAINT fk_booktour Foreign Key (U_ID) REFERENCES Users(U_ID)
Alter Table Users ADD CONSTRAINT fk_userroles Foreign Key (U_Role) REFERENCES UserRole(UR_ID)
