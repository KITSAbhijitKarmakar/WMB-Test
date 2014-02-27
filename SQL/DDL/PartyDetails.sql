----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	PartyDetails.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
create table MBREPOS.PARTYDETAILS (
GENERICSTORECODE        VARCHAR(50),
VATNUMBER               VARCHAR(50),
EMAIL                   VARCHAR(50),
COUNTRY                 VARCHAR(50),
POSTCODE                VARCHAR(50),
CITY                    VARCHAR(50),
PARTYUNIQUEID           VARCHAR(50),
FUNCTION                VARCHAR(50),
ADDRESSLINE4            VARCHAR(50),
ADDRESSLINE3            VARCHAR(50),
ADDRESSLINE2            VARCHAR(50),
ADDRESSLINE1            VARCHAR(50),
REGION                  VARCHAR(50),
PARTYREFNUMBER          VARCHAR(50),
SOLVESTOREID            VARCHAR(50),
TELEPHONE               VARCHAR(50),
CONTACTNAME             VARCHAR(50),
PARTYID                 VARCHAR(50),
VATTYPE                 VARCHAR(50),
COMPANY                 VARCHAR(50))
TABLESPACE "MBODS" NOCOMPRESS LOGGING;
