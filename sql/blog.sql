create table ms_article
(
    id             bigint auto_increment
        primary key,
    comment_counts int          null comment '评论数量',
    create_date    bigint       null comment '创建时间',
    summary        varchar(255) null comment '简介',
    title          varchar(64)  null comment '标题',
    view_counts    int          null comment '浏览数量',
    weight         int          not null comment '是否置顶',
    author_id      bigint       null comment '作者id',
    body_id        bigint       null comment '内容id',
    category_id    bigint       null comment '类别id'
)
    charset = utf8mb3;

create table ms_article_body
(
    id           bigint auto_increment
        primary key,
    content      longtext null,
    content_html longtext null,
    article_id   bigint   not null
)
    charset = utf8mb3;

create index article_id
    on ms_article_body (article_id);

create table ms_article_tag
(
    id         bigint auto_increment
        primary key,
    article_id bigint not null,
    tag_id     bigint not null
)
    charset = utf8mb3;

create index article_id
    on ms_article_tag (article_id);

create index tag_id
    on ms_article_tag (tag_id);

create table ms_category
(
    id            bigint auto_increment
        primary key,
    avatar        varchar(255) collate utf8mb4_unicode_ci null,
    category_name varchar(255) collate utf8mb4_unicode_ci null,
    description   varchar(255) collate utf8mb4_unicode_ci null
)
    charset = utf8mb3;

create table ms_comment
(
    id          bigint auto_increment
        primary key,
    content     varchar(255) collate utf8mb4_unicode_ci not null,
    create_date bigint                                  not null,
    article_id  bigint                                  not null,
    author_id   bigint                                  not null,
    parent_id   bigint                                  not null,
    to_uid      bigint                                  not null,
    level       int                                     not null
)
    charset = utf8mb3;

create index article_id
    on ms_comment (article_id);

create table ms_sys_user
(
    id                  bigint auto_increment
        primary key,
    account             varchar(64)  null comment '账号',
    admin               bit          null comment '是否管理员',
    avatar              varchar(255) null comment '头像',
    create_date         bigint       null comment '注册时间',
    deleted             bit          null comment '是否删除',
    email               varchar(128) null comment '邮箱',
    last_login          bigint       null comment '最后登录时间',
    mobile_phone_number varchar(20)  null comment '手机号',
    nickname            varchar(255) null comment '昵称',
    password            varchar(64)  null comment '密码',
    salt                varchar(255) null comment '加密盐',
    status              varchar(255) null comment '状态'
)
    charset = utf8mb3;

create table ms_tag
(
    id       bigint auto_increment
        primary key,
    avatar   varchar(255) collate utf8mb4_unicode_ci null,
    tag_name varchar(255) collate utf8mb4_unicode_ci null
)
    charset = utf8mb3;

