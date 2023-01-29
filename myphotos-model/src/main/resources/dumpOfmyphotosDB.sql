DROP TABLE IF EXISTS photo cascade;
DROP TABLE IF EXISTS access_token cascade;
DROP TABLE IF EXISTS profile cascade;

CREATE TABLE profile
(
    id              bigint primary key                           not null,
    uid             varchar(255) unique                          not null,
    email           varchar(100) unique                          not null,
    first_name      varchar(60)                                  not null,
    last_name       varchar(60)                                  not null,
    avatar_url      varchar(255)                                 not null,
    job_title       varchar(100)                                 not null,
    location        varchar(100)                                 not null,
    rating          int                            default 0     not null,
    photo_count     int                            default 0     not null,
    date_of_created timestamp(0) without time zone default now() not null
);

CREATE TABLE access_token
(
    token           text primary key                             not null,
    profile_id      bigint                                       not null,
    date_of_created timestamp(0) without time zone default now() not null,

    FOREIGN KEY (profile_id) REFERENCES profile (id) ON DELETE restrict ON UPDATE restrict
);

CREATE TABLE photo
(
    id                 bigint primary key                           not null,
    profile_id         bigint                                       not null,
    url_to_original    varchar(255)                                 not null,
    url_to_small       varchar(255)                                 not null,
    url_to_large       varchar(255)                                 not null,
    count_of_views     bigint                         default 0     not null,
    count_of_downloads bigint                         default 0     not null,
    date_of_created    timestamp(0) without time zone default now() not null,

    FOREIGN KEY (profile_id) REFERENCES profile (id) ON DELETE restrict ON UPDATE restrict
);

CREATE SEQUENCE if not exists profile_seq
    START 1
    INCREMENT 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE profile_seq OWNER TO myphotos;
SELECT pg_catalog.setval('profile_seq', 1, false);

CREATE SEQUENCE if not exists photo_seq
    START 1
    INCREMENT 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE photo_seq OWNER TO myphotos;
SELECT pg_catalog.setval('photo_seq', 1, false);

CREATE FUNCTION update_rating() RETURNS VOID
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    UPDATE profile
    SET rating = stat.rating
    FROM (SELECT profile_id,
                 sum(count_of_views * 1 + count_of_downloads * 100)
                     AS rating
          FROM photo
          GROUP BY profile_id) AS stat
    WHERE profile.id = stat.profile_id;
END;
$$;
ALTER FUNCTION update_rating() OWNER TO myphotos;
