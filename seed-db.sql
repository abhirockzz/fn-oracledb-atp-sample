create table EMPLOYEES (    
  EMP_EMAIL    varchar2(100),  
  EMP_NAME    varchar2(100),  
  EMP_DEPT    varchar2(50),
  constraint pk_emp primary key (EMP_EMAIL)
)