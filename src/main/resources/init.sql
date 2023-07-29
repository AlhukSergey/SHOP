DROP SCHEMA IF EXISTS shop;
CREATE SCHEMA IF NOT EXISTS shop;

DROP TABLE IF EXISTS shop.users;
CREATE TABLE IF NOT EXISTS shop.users
(
    id       INT            NOT NULL AUTO_INCREMENT,
    name     VARCHAR(45)    NOT NULL,
    surname  VARCHAR(60)    NOT NULL,
    birthday Timestamp      NOT NULL,
    balance  DECIMAL(10, 2) NOT NULL,
    email    VARCHAR(45)    NOT NULL,
    password VARCHAR(45)    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX IDX_USERS_USER_ID_UNIQUE (id ASC),
    UNIQUE INDEX IDX_USERS_EMAIL_UNIQUE (email ASC),
    UNIQUE INDEX IDX_USERS_PASSWORD_UNIQUE (password ASC)
);


INSERT INTO shop.users(name, surname, birthday, balance, email, password)
VALUES ('Anna',
        'Ivanova',
        '1990-12-12',
        0.00,
        'anna18@gmail.com',
        'QSExKz1hc2FzYXNhYXM=');



DROP TABLE IF EXISTS shop.categories;
CREATE TABLE IF NOT EXISTS shop.categories
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX IDX_CATEGORIES_CATEGORY_ID_UNIQUE (id ASC),
    UNIQUE INDEX IDX_CATEGORIES_NAME_UNIQUE (name ASC)
);


INSERT INTO shop.categories(name)
VALUES ('Головные уборы и аксессуары');
INSERT INTO shop.categories(name)
VALUES ('Сумки');
INSERT INTO shop.categories(name)
VALUES ('Спортивная обувь');
INSERT INTO shop.categories(name)
VALUES ('Перчатки');
INSERT INTO shop.categories(name)
VALUES ('Спортивный инвентарь');
INSERT INTO shop.categories(name)
VALUES ('Термобелье');


DROP TABLE IF EXISTS shop.products;
CREATE TABLE IF NOT EXISTS shop.products
(
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(90)  NOT NULL,
    description VARCHAR(300) NOT NULL,
    price       DOUBLE       NOT NULL,
    categoryId  INT          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX IDX_PRODUCTS_ID_UNIQUE (id ASC),
    CONSTRAINT FK_PRODUCTS_CATEGORY_ID_CATEGORIES_ID
        FOREIGN KEY (categoryId)
            REFERENCES shop.categories (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Жокейка (кепка) спортивная мужская/женская Columbia',
        'Бейсболка от Columbia незаменима для летнего активного отдыха на природе. В 4 из 6 стандартных панелей этой классической модели используется легкая сетчатая ткань. Натуральный хлопок с небольшим добавлением эластана приятен на ощупь и хорошо пропускает воздух даже в жару.',
        55.5,
        1);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Жокейка (кепка) для мальчиков FILA',
        'Бейсболка FILA пригодится ребенку во время активных игр на улице. Модель, выполненная из натурального хлопка, гарантирует комфорт в жаркую погоду.',
        25.84,
        1);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Мешок для обуви для девочек FILA',
        'Практичный мешок для обуви от FILA. Модель выполнена из прочного полиэстера. Удобный карман позволит захватить с собой необходимые мелочи.',
        21.8,
        2);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Сумка мужская/женская FILA',
        'Вместительная сумка-шоппер из прочного хлопка. Предусмотрены длинные ручки, чтобы носить ее и в руках, и на плече.',
        27,
        2);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Сумка мужская/женская FILA',
        'Сумка кросс-боди от бренда FILA поможет держать все важные мелочи под рукой, когда вы отправляетесь по делам или на тренировку. Найти нужные вещи несложно, так как в модели предусмотрено основное отделение и 2 дополнительных кармана.',
        32.3,
        2);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Полуботинки мужские GSD',
        'Кроссовки GSD One подойдут для прогулок по городу.',
        63.3,
        3);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Полуботинки мужские Head',
        'Кроссовки Head Leon X Tms Fluo пригодятся для комфортных и размеренных пробежек в свое удовольствие.',
        240.5,
        3);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Полуботинки мужские Reebok',
        'Бескомпромиссно удобные кроссовки Reebok Glide Ripple Clip — идеальный вариант на каждый день. Узнаваемый минималистичный дизайн дополнен деталями, которые отсылают к архивам бренда.',
        296,
        3);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Перчатки мужские/женские Northland',
        'Перчатки Northland для треккинговых походов выполнены из материала с технологией защиты от УФ-излучения. Нескользящие накладки обеспечивают надежный хват.',
        39.5,
        4);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Фляга Stern',
        'Фляга Stern объемом 700 мл позволит держать воду под рукой во время велосипедных прогулок.',
        13.04,
        5);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Насос Stern',
        'Велосипедный насос Stern.',
        15.61,
        5);
INSERT INTO shop.products(name, description, price, categoryId)
VALUES ('Брюки женские Glissade',
        'Женские термокальсоны Glissade станут отличной защитой от холода во время занятий зимними видами спорта. Модель рекомендована для низкого уровня физической активности.',
        84,
        6);

DROP TABLE IF EXISTS shop.orders;
CREATE TABLE IF NOT EXISTS shop.orders
(
    id         INT         NOT NULL AUTO_INCREMENT,
    userId     INT         NOT NULL,
    createdAt Timestamp   NOT NULL,
    status     varchar(45) NOT NULL,
    price      DOUBLE      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX IDX_ORDERS_ID_UNIQUE (id ASC),
    CONSTRAINT FK_ORDERS_USER_ID_USERS_ID
        FOREIGN KEY (userId)
            REFERENCES shop.users (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

DROP TABLE IF EXISTS shop.order_lists;
CREATE TABLE IF NOT EXISTS shop.order_lists
(
    orderId   INT NOT NULL,
    productId INT NOT NULL,
    quantity  INT NOT NULL DEFAULT 0,
    PRIMARY KEY (orderId, productId),
    CONSTRAINT FK_ORDERS_PRODUCTS_ORDER_ID_ORDERS_ID
        FOREIGN KEY (orderId)
            REFERENCES orders (id),
    CONSTRAINT FK_ORDERS_PRODUCTS_PRODUCT_ID_PRODUCTS_ID
        FOREIGN KEY (productId)
            REFERENCES products (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

DROP TABLE IF EXISTS shop.images;
CREATE TABLE IF NOT EXISTS shop.images
(
    id           INT          NOT NULL AUTO_INCREMENT,
    imagePath    VARCHAR(150) NOT NULL,
    categoryId   INT          NULL,
    productId    INT          NULL,
    primaryImage INT          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX IDX_IMAGES_ID_UNIQUE (id ASC),
    CONSTRAINT FK_IMAGES_CATEGORIES_ID_CATEGORIES_ID
        FOREIGN KEY (categoryId)
            REFERENCES shop.categories (id),
    CONSTRAINT FK_IMAGES_PRODUCTS_ID_PRODUCTS_ID
        FOREIGN KEY (productId)
            REFERENCES shop.products (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/4ce/800_800_a906/69910370299.jpg', 1, null, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/274/800_800_a74b/49454760299.jpg', 2, null, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/9fe/800_800_521c/78081760299.jpg', 3, null, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/e98/800_800_2f68/73899710299.jpg', 4, null, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/538/800_800_fbb7/56802650299.jpg', 5, null, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/d90/800_800_fce4/54484920299.jpg', 6, null, 1);


INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/f55/395_380_6a47/72193170299.jpg', null, 1, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/7c7/395_380_93a4/72033820299.jpg', null, 1, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/fa4/395_380_6817/72033830299.jpg', null, 1, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/89f/395_380_689e/72033840299.jpg', null, 1, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/cd2/395_380_7aa0/76052220299.jpg', null, 2, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/762/395_380_cd0a/74872040299.jpg', null, 2, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/2ab/395_380_da90/74872060299.jpg', null, 2, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/79a/395_380_cdc4/74872080299.jpg', null, 2, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/efd/395_380_d2a2/81122110299.jpg', null, 3, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/76f/395_380_de59/80813620299.jpg', null, 3, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/ac7/395_380_cf08/80813640299.jpg', null, 3, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/b49/395_380_3a47/74206550299.jpg', null, 4, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/c0f/67_67_e8e3/73987050299.jpg', null, 4, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/773/395_380_648e/73987060299.jpg', null, 4, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/123/395_380_40ae/74428530299.jpg', null, 5, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/4c3/395_380_62f8/74059270299.jpg', null, 5, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/2a0/395_380_ff32/74059280299.jpg', null, 5, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/6e3/395_380_f6a8/78073780299.jpg', null, 6, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/9fa/395_380_5dc3/53586380299.jpg', null, 6, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/285/395_380_6951/52213660299.jpg', null, 6, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/5c4/395_380_10ad/80797320299.jpg', null, 7, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/91e/395_380_f9ae/80567730299.jpg', null, 7, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/f14/395_380_2d03/80567740299.jpg', null, 7, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/aa0/395_380_d63e/80567760299.jpg', null, 7, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/ee7/395_380_cd6f/82070640299.jpg', null, 8, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/c36/395_380_51fa/79724880299.jpg', null, 8, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/33f/395_380_0545/79697890299.jpg', null, 8, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/ae2/395_380_2047/79697900299.jpg', null, 8, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/77a/395_380_d588/73899720299.jpg', null, 9, 1);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/a37/395_380_f6fe/61381440299.jpg', null, 10, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/0ba/395_380_cdb3/59177450299.jpg', null, 10, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/871/395_380_653a/57988220299.jpg', null, 11, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/57d/395_380_acf5/49929070299.jpg', null, 11, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/4f4/395_380_c9a4/49929080299.jpg', null, 11, 0);

INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/e2e/395_380_caef/54563010299.jpg', null, 12, 1);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/7d4/395_380_7bb9/54484910299.jpg', null, 12, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/d90/395_380_5c02/54484920299.jpg', null, 12, 0);
INSERT INTO shop.images (imagePath, categoryId, productId, primaryImage)
VALUES ('https://cdnby.sportmaster.com/upload/mdm/media_content/resize/7ab/395_380_d636/54484930299.jpg', null, 12, 0);

DROP TABLE IF EXISTS shop.statistic;
CREATE TABLE IF NOT EXISTS shop.statistic
(
    id          INT          NOT NULL AUTO_INCREMENT,
    description VARCHAR(300) NOT NULL,
    PRIMARY KEY (id)
);