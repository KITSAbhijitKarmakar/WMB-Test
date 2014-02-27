----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	PayerAuthData.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------

create table MBREPOS.PAYERAUTHDATA (
AUTHENTICATIONECI       	VARCHAR(20),
ATSDATA                 	VARCHAR(10),
CARDSCHEMEPROVIDER      	VARCHAR(20),
PAYERAUTHREQUESTIDPRESENT 	VARCHAR(20),
AUTHENTICATIONCAVVPRESENT 	VARCHAR(20),
AUTHENTICATIONSTATUS    	VARCHAR(20))
TABLESPACE "MBODS" NOCOMPRESS LOGGING;
