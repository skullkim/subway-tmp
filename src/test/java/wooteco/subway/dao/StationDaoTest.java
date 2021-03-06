package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;

@DisplayName("Station Dao를 통해서")
@JdbcTest
@Transactional
class StationDaoTest {

    private static final Station STATION_FIXTURE = new Station(1L, "선릉역");
    private static final Station STATION_FIXTURE2 = new Station(2L, "강남역");
    private static final Station STATION_FIXTURE3 = new Station(3L, "역삼역");

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setup() {
        stationDao = new StationDao(namedParameterJdbcTemplate, dataSource);
    }

    @Nested
    @DisplayName("새로운 역을 저장할 때")
    class SaveTest {

        @Test
        @DisplayName("역 이름이 중복되지 않으면 저장할 수 있다.")
        void save_Success_If_Not_Exists() {
            assertThatCode(() -> stationDao.save(STATION_FIXTURE))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("역 이름이 중복되면 예외가 발생한다.")
        void save_Fail_If_Exists() {
            stationDao.save(STATION_FIXTURE);
            assertThatThrownBy(() -> stationDao.save(STATION_FIXTURE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 지하철역입니다. Station{name='선릉역'}");
        }
    }


    @Test
    @DisplayName("전체 지하철 역을 조회할 수 있다")
    void findAll() {
        stationDao.save(STATION_FIXTURE);
        stationDao.save(STATION_FIXTURE2);
        stationDao.save(STATION_FIXTURE3);

        assertThat(stationDao.findAll()).isEqualTo(List.of(STATION_FIXTURE, STATION_FIXTURE2, STATION_FIXTURE3));
    }

    @Nested
    @DisplayName("아이디로 지하철역을 삭제할 떄")
    class DeleteStationTest {

        @Test
        @DisplayName("아이디가 존재하면 아이디로 지하철역을 삭제할 수 있다")
        void deleteById() {
            final Station station = stationDao.save(STATION_FIXTURE);
            final List<Station> stations = stationDao.findAll();
            stationDao.deleteById(station.getId());
            final List<Station> afterDelete = stationDao.findAll();

            assertThat(stations).isNotEmpty();
            assertThat(afterDelete).isEmpty();
        }

        @Test
        @DisplayName("아이디가 존재하지 않으면 예외를 던진다.")
        void delete_By_Id_Fail() {
            assertThatThrownBy(() -> stationDao.deleteById(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("요청한 지하철 역이 존재하지 않습니다. id=1");
        }
    }

}
