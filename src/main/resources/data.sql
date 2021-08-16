insert into adv_content (
    id,
    bonus,
    head_top,
    price,
    price_comment,
    desc_head,
    description,
    bullets,
    broker_name,
    broker_comment,
    coordinates)
    values (
    '1',
    '500000',
    'Продажа квартиры в историческом районе',
    '105 000 000 руб.',
    'Возможна покупка в ипотеку',
    'Квартира с красивым видом',
    'Великое дело, главный труд жизни.\n \nСамое значимое и эпохальное творение человека в искусстве, ' ||
     'науке или любой другой сфере деятельности.\n' ||
      'Клубный дом Magnum на Усачева, 9 в Хамовниках - это новое явление, которое, несомненно, ' ||
       'окажет влияние на сферу московской недвижимости премиум-класса, новое слово в сегменте.\n \n' ||
        'Концептуальный и эксклюзивный проект, свободный от условностей. Этот дом цепляет и внешним видом.\n \n' ||
         'Нетипичная для района архитектура, особый характер: темный камень, необычная фактура, ' ||
          'металл и стекло - брутальное и строгое здание, но с собственной харизмой и индивидуальностью.',
    '2 км от Кремля|Закрытая территория|Всего 20 квартир|Подземный паркинг',
    'Светлана Горбунова (La Prima Estate)',
    'брокер объекта',
    '56.005727, 37.647179'
     ) ON CONFLICT DO NOTHING;


insert into role (id, name) values ('1', 'admin') ON CONFLICT DO NOTHING;
insert into role (id, name) values ('2', 'manager') ON CONFLICT DO NOTHING;

insert into manager (id, login, password, email, role_id) values ('1', 'test', 'Secret13', 'mrdmitrypavlov@yandex.ru', '1') ON CONFLICT DO NOTHING;

insert into adv(id, url, adv_content_id, creating_date, manager_id)  values ('1', 'test-flat', '1', current_date, '1') ON CONFLICT DO NOTHING;
