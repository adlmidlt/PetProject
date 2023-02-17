package ru.zavodsvet.fgw_desktop_v2.common

// Общие.
const val INFO_FOR_IT = "Обратитесь в отдел ИТ!"

// Приложение.
const val APP_START = "Приложение запущено"
const val APP_STOP = "Приложение остановлено"
const val APP_CLOSE = "Приложение будет закрыто!"

// База данных.
const val DB_CONNECTED = "Соединение с БД установлено"
const val DB_CONN_CLOSED = "Соединение с БД закрыто"

const val SQL_CLOSE = "SQL закрыт."

const val TEXT_EVENT_EMPLOYEE_RECORDED = "Событие сотрудника записаны!"
const val TEXT_DELETE_ORDER = "Удалить ордер"
const val TEXT_CREATE_ORDER = "Сформировать ордер"

// логи
const val MAKE_ORDER_SUCCESS = "Ордер сформирован"

// Оповещение о событии.
const val INFO_TEXT = "Информация"
const val ERROR_TEXT = "Ошибка"
const val WARNING_TEXT = "Предупреждение"
const val INCORRECTLY_ENTERED_LOGIN_OR_PASSWD = "Не верно введен логин или пароль"
const val WRONG_FORMAT_DATA = "Заполните все поля или проверьте корректность введённых данных"
const val PASSWORDS_NOT_MATCH = "Пароли не совпадают, проверти корректность ввода пароля"
const val PASSWORD_EDIT = "Пароль успешно изменён!"
const val ALL_GOOD_WORK_NEXT = "Все хорошо, работаем дальше!"
const val FILE_SUCCESS_SAVE = "Файл успешно выгружен."
const val NOT_FOUND_FILE_OR_OPEN_NOW = "Процесс не может получить доступ к файлу, так как этот файл занят другим процессом. Закройте отчет и попробуйте заново."
const val OPER_WITH_ZERO_NOT_EXIST = "Операции с \"идентификатором 0\" не существует"

// Название окон
const val TITLE_WINDOW_TLK = "Товарно-логистический комплекс"
const val TITLE_AUTH_TLK = "Авторизация ТЛК"
const val TITLE_CHANGE_PASSWORD = "Изменение пароля"

const val TITLE_ALL_OPERATION = "Все операции"
const val TITLE_CARD_OPERATION = "Карточка операции"
const val TITLE_TICKET = "Просмотр этикетки"
const val TITLE_MANUFACTURING_PRODUCT = "Производство продукции"
const val TITLE_BIND_PERMIT = "Привязать пропуск?"
const val TITLE_CARD_SPECIFICATION = "Карточка спецификации"
const val TITLE_ADD_SPEC = "Добавить спецификацию..."
const val TITLE_CARD_SP_TEST_REPORT = "Карточка спецификации протокола испытаний"
const val TITLE_CARD_TEST_REPORT = "Карточка протокола испытаний"
const val TITLE_TEST_REPORT = "Протокола испытаний"

const val TITLE_BALANCES = "Остатки"
const val TITLE_TURNOVER_SHEET = "Оборотная ведомость"

const val TITLE_LOC_STORE = "Участки хранения"
const val TITLE_PROD_PACK = "Варианты упаковки"
const val TITLE_CARD_PROD_PACK = "Карточка варианта упаковки"
const val TITLE_CARD_LOC_STORE = "Карточка участка хранения"
const val TITLE_KD_NAME = "Конструкторские наименования"
const val TITLE_VID_OPERATION = "Виды операции"
const val TITLE_EMPLOYEE = "Сотрудники"

const val TITLE_CREATE_OPERATION = "Формирование операции..."

// стиль для текстового поля.
const val BORDER_AN_INVALID_FIELD = "-fx-border-color: red"
const val NOT_CORRECT_FORMAT = "> 1 символа (.) запрещено"
// CSS
const val COLOR_TEXT_BLUE = "-fx-text-fill: blue;"

// дата время
const val TWO_DIGIT_NUMBER = 10
const val MAX_YEAR = 2079
const val MIN_YEAR = 1950

const val NOT_CORRECT_YEAR = "Некорректный год (1950..2079)"
const val FORMAT_YEAR = "Формат года (гггг)"

const val NOT_CORRECT_MONTH_OF_YEAR = "Некорректный месяц (01..12)"
const val FORMAT_MONTH = "Формат месяца (мм)"

const val NOT_CORRECT_DAY_OF_MONTH = "Некорректный день (01..31)"
const val FORMAT_DAY = "Формат дня (дд)"

const val FORMAT_HOURS_OF_DAY = "Формат часа (чч)"
const val NOT_CORRECT_HOURS = "Некорректные часы (00..23)"

const val NOT_CORRECT_MINUTES = "Некорректные минуты (01..59)"
const val FORMAT_MINUTES_OF_HOUR = "Формат минут (мм)"

// Виды операции по номерам.
const val ARRIVAL = 0   // Приход.
const val MOVEMENT = 1  // Перемещение.
const val DEBIT = 2     // Списание.
const val SALE = 3      // Продажа.
const val INVENTORY = 4 // Инвентаризация.
const val REGRADING = 5 // Пересортица.
const val REPACKING = 6 // Перетарка.

const val TITLE_LOC_TO = "КУДА"
const val TITLE_FROM_TO = "ОТКУДА"
const val TITLE_WHERE = "Где"
const val TITLE_FOR_ARRIVAL = "Для прихода"
const val TITLE_FOR_DEBIT = "Для списания"