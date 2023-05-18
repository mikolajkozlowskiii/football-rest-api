insert into leagues (id, name, season, country)
values (1, 'Premier League', '2022/23', 'ENG');

insert into leagues (id, name, season, country)
values (2, 'LaLiga', '2019/20', 'ESP');

insert into leagues (id, name, season, country)
values (3, 'LaLiga', '2020/21', 'ESP');

insert into teams(id, name)
values (1, 'Manchester UTD');

insert into teams(id, name)
values (2, 'Manchester City');

insert into teams(id, name)
values (3, 'FC Barcelona');

insert into teams(id, name)
values (4, 'Real Madrid');

insert into teams(id, name)
values (5, 'Getafe');

insert into teams(id, name)
values (6, 'Villareal');

INSERT INTO matches (id, away_team_id, home_team_id, league_id, home_team_score, away_team_score, date_time)
VALUES (1, 1, 2, 1, 0, 0, '2023-05-08T13:30:00');

INSERT INTO matches (id, away_team_id, home_team_id, league_id, home_team_score, away_team_score, date_time)
VALUES (2, 2, 1, 1, 3, 2, '2023-05-17T17:00:00');

INSERT INTO matches (id, away_team_id, home_team_id, league_id, home_team_score, away_team_score, date_time)
VALUES (3, 3, 4, 2, 0, 1, '2023-05-17T17:15:00');

INSERT INTO matches (id, away_team_id, home_team_id, league_id, home_team_score, away_team_score, date_time)
VALUES (4, 4, 3, 2, 0, 0, '2023-05-17T13:00:00');

INSERT INTO matches (id, away_team_id, home_team_id, league_id, home_team_score, away_team_score, date_time)
VALUES (5, 3, 4, 3, 5, 1, '2021-11-19T21:00:00');