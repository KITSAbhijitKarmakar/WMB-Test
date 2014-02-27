----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	TenderData.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
alter table MBREPOS.TENDERDATA add(
PEDTENDERID             VARCHAR2(255),
VERIFONETENDERID        VARCHAR2(255),
BANKINGDESCRIPTION      VARCHAR2(255),
ACQUIRERNAME            VARCHAR2(255),
GLACCOUNT               VARCHAR2(255),
APPSTENDERDESCRIPTION   VARCHAR2(255),
ACQUIRERIDNONUKCANDP    VARCHAR2(255),
PANDORATENDERTYPE       VARCHAR2(255),
RECONCILIATIONFLAG      VARCHAR2(255),
GLTENDERTYPE            VARCHAR2(255),
CCCPTENDERDESCRIPTION   VARCHAR2(255),
APPSTENDERTYPE          VARCHAR2(255),
ACQUIRERNAMENONUK       VARCHAR2(255),
TENDERDESCRIPTION       VARCHAR2(255),
ACQUIRERIDNONUK         VARCHAR2(255),
CCCPTENDERTYPE          VARCHAR2(255),
ACQUIRERIDCANDP         VARCHAR2(255),
ACCOUNTNUMBER           VARCHAR2(255),
SETTLEMENTDESCRIPTION   VARCHAR2(255),
FINANCECODE             VARCHAR2(255),
HEXFORMAT               VARCHAR2(255),
ACQUIRERID              VARCHAR2(255))
