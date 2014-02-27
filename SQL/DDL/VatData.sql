----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	VatData.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
create table MBREPOS.VATDATA (
VERIFONEVATCODE         VARCHAR2(255),
EPOSVATCODE             VARCHAR2(255),
GENERICVATCODE          VARCHAR2(255),
IBSDEALSVATCODE         VARCHAR2(255),
RJCOUNTRYCODE           VARCHAR2(255),
RJVATCATEGORY           VARCHAR2(255),
SAPCOUNTRYCODE          VARCHAR2(255),
SAPVATCODE              VARCHAR2(255),
SAPARTCOUNTRYCODE       VARCHAR2(255),
SAPARTVATCODE           VARCHAR2(255),
SAPMDVATCODE            VARCHAR2(255),
SAPMDVATCATEGORY        VARCHAR2(255),
SOLVEVATCODE            VARCHAR2(255),
TRFVATCODE              VARCHAR2(255))
TABLESPACE "MBODS" NOCOMPRESS LOGGING;
