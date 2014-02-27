----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	MbDelType.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------


create table MBREPOS.MBDELTYPE (
SAPDELTYPE              VARCHAR2(255),
WCSSFLOW       		VARCHAR2(255),
WCSSFLOWDESC   		VARCHAR2(255))
TABLESPACE "MBODS" NOCOMPRESS LOGGING; 
