----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	MBSTRCD.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
create table MBREPOS.MBSTRCD (
CANVAL			VARCHAR2(255),
NUMSTORECODE            VARCHAR2(255),
CHARSTORECODE	      VARCHAR2(255),
FULLSTORECODE           VARCHAR2(255),
STOREDIVISION		VARCHAR2(255),
NONMFSTORE			VARCHAR2(255),
GLREGIONCOLUMN		VARCHAR2(255),
STORENAME			VARCHAR2(255),
MCSTORE		      VARCHAR2(255),
TRADECOUNTERFLAG        VARCHAR2(255),
VPCTRADINGREGION   	VARCHAR2(255),
STOREOPENDATETIME   	VARCHAR2(255),
TESTORE			VARCHAR2(255),
SAPCOSTORE	      	VARCHAR2(255),
ICCSTORE            	VARCHAR2(255),
STORETYPE   		VARCHAR2(255),
GLREGION          	VARCHAR2(255),
OLASOURCEID       	VARCHAR2(255),
NONWMSRCC      		VARCHAR2(255),
GENERICSTORECODE       	VARCHAR2(255),
PRICEFORSTORECODE       VARCHAR2(255))
TABLESPACE "MBODS" NOCOMPRESS LOGGING;
