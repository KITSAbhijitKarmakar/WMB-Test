----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	MBOrdType.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------


create table MBREPOS.MBORDTYP (
ISSUINGSTORAGELOCATION  VARCHAR2(255),
ORDERTYPE      		VARCHAR2(255),
DELIVERYTYPE       	VARCHAR2(255))
TABLESPACE "MBODS" NOCOMPRESS LOGGING; 
