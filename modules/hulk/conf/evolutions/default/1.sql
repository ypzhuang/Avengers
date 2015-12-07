# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table t_action (
  id                        bigint auto_increment not null,
  module                    varchar(255) not null,
  uri                       varchar(255) not null,
  mapping_action            varchar(255) not null,
  method                    varchar(255) not null,
  constraint pk_t_action primary key (id))
;

create table t_user_n_machine (
  id                        bigint auto_increment not null,
  uid                       bigint,
  machineid                 varchar(255),
  create_time               datetime,
  client_type               varchar(255),
  constraint pk_t_user_n_machine primary key (id))
;

create table t_doctor (
  id                        bigint auto_increment not null,
  constraint pk_t_doctor primary key (id))
;

create table t_fail_reason (
  id                        bigint auto_increment not null,
  desciption                varchar(500),
  content                   varchar(2000),
  create_time               datetime,
  is_delete                 tinyint(1) default 0,
  suggest                   varchar(1000),
  constraint pk_t_fail_reason primary key (id))
;

create table t_pic_file_history (
  id                        bigint auto_increment not null,
  creattime                 datetime,
  storagepath               varchar(255),
  partnum                   integer,
  lid                       bigint,
  ltr_id                    bigint,
  size_info                 varchar(255),
  thumb_path                varchar(255),
  rotation                  integer,
  rotated                   integer,
  create_date               datetime,
  constraint pk_t_pic_file_history primary key (id))
;

create table t_hospital (
  id                        bigint auto_increment not null,
  lat                       varchar(255),
  lon                       varchar(255),
  name                      varchar(255),
  address                   varchar(500),
  location                  varchar(255),
  constraint pk_t_hospital primary key (id))
;

create table t_indicator (
  id                        bigint auto_increment not null,
  showname                  varchar(255),
  code                      varchar(255),
  description               varchar(1000),
  createtime                datetime,
  category                  integer,
  unit                      varchar(255),
  type                      integer,
  is_doctor_filter          tinyint(1) default 0,
  log_in_slider             tinyint(1) default 0,
  upperlimit                bigint,
  lowerlimit                bigint,
  constraint pk_t_indicator primary key (id))
;

create table t_indicator_value (
  id                        bigint auto_increment not null,
  iid                       bigint,
  category                  integer,
  name                      varchar(255),
  cid                       bigint,
  value                     double,
  state                     integer,
  type                      integer,
  upperlimit                double,
  lowerlimit                double,
  direction                 integer,
  calmethod                 integer,
  uid                       bigint,
  testtime                  datetime,
  ltr_id                    bigint,
  is_delete                 integer,
  standard_value            double,
  constraint pk_t_indicator_value primary key (id))
;

create table t_ltr (
  id                        bigint auto_increment not null,
  uid                       bigint,
  hid                       bigint,
  hosname                   varchar(255),
  testtime                  datetime,
  createtime                datetime,
  category                  integer,
  finish                    integer,
  is_delete                 tinyint(1) default 0,
  fail_id                   bigint,
  forage                    varchar(255),
  source                    integer,
  state                     integer,
  dis_id                    bigint,
  is_viewed_by_me           tinyint(1) default 0,
  viewed_doctor_ids         varchar(255),
  type                      integer,
  status                    integer,
  agent_info                varchar(255),
  constraint ck_t_ltr_status check (status in (0,1,2,3,4,5,6,7,8)),
  constraint pk_t_ltr primary key (id))
;

create table t_pic_file (
  id                        bigint auto_increment not null,
  creattime                 datetime,
  storagepath               varchar(255),
  partnum                   integer,
  lid                       bigint,
  ltr_id                    bigint,
  size_info                 varchar(255),
  thumb_path                varchar(255),
  rotation                  integer,
  rotated                   integer,
  constraint pk_t_pic_file primary key (id))
;

create table product (
  id                        bigint auto_increment not null,
  ean                       bigint,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_product primary key (id))
;

create table t_sys_user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  phone                     varchar(255),
  password                  varbinary(64) not null,
  auth_token                varbinary(64),
  temp_token                varchar(255),
  is_deleted                tinyint(1) default 0,
  created_date              datetime,
  last_login_time           datetime,
  role                      integer,
  constraint ck_t_sys_user_role check (role in (0,1,2,3)),
  constraint uq_t_sys_user_email unique (email),
  constraint uq_t_sys_user_phone unique (phone),
  constraint uq_t_sys_user_auth_token unique (auth_token),
  constraint pk_t_sys_user primary key (id))
;

alter table t_indicator_value add constraint fk_t_indicator_value_indicator_1 foreign key (iid) references t_indicator (id) on delete restrict on update restrict;
create index ix_t_indicator_value_indicator_1 on t_indicator_value (iid);
alter table t_indicator_value add constraint fk_t_indicator_value_ltr_2 foreign key (ltr_id) references t_ltr (id) on delete restrict on update restrict;
create index ix_t_indicator_value_ltr_2 on t_indicator_value (ltr_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table t_action;

drop table t_user_n_machine;

drop table t_doctor;

drop table t_fail_reason;

drop table t_pic_file_history;

drop table t_hospital;

drop table t_indicator;

drop table t_indicator_value;

drop table t_ltr;

drop table t_pic_file;

drop table product;

drop table t_sys_user;

SET FOREIGN_KEY_CHECKS=1;

