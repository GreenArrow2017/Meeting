package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:42 AM
 */
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI {

    @Resource
    private BannerTMapper bannerTMapper;
    @Resource
    private FilmTMapper filmTMapper;
    @Resource
    private CatDictTMapper catDictTMapper;
    @Resource
    private SourceDictTMapper sourceDictTMapper;
    @Resource
    private YearDictTMapper yearDictTMapper;
    @Resource
    private FilmInfoTMapper filmInfoTMapper;
    @Resource
    private ActorTMapper actorTMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> result = new ArrayList<>();
        List<BannerT> bannerTS = bannerTMapper.selectList(null);
        for (BannerT bannerT : bannerTS) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(bannerT.getUuid() + "");
            bannerVO.setBannerUrl(bannerT.getBannerUrl());
            bannerVO.setBannerAddress(bannerT.getBannerAddress());
            result.add(bannerVO);
        }
        return result;
    }

    private List<FilmInfo> getFilmInfo(List<FilmT> films) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (FilmT filmT : films) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(filmT.getFilmScore());
            filmInfo.setImgAddress(filmT.getImgAddress());
            filmInfo.setFilmType(filmT.getFilmType());
            filmInfo.setFilmScore(filmT.getFilmScore());
            filmInfo.setFilmName(filmT.getFilmName());
            filmInfo.setFilmId(filmT.getUuid() + "");
            filmInfo.setExpectNum(filmT.getFilmPresalenum());
            filmInfo.setBoxNum(filmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(filmT.getFilmTime()));
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        if (isLimit) {
            Page<FilmT> page = new Page<>(1, nums);
            List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfo(films);
            filmVO.setFilmNum(films.size());
            filmVO.setFilmInfo(filmInfos);
        } else {
            Page<FilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;


            }
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfo(films);
            filmVO.setFilmNum(films.size());

            int totalCounts = filmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts / nums) + 1;


            filmVO.setFilmInfo(filmInfos);
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        if (isLimit) {
            Page<FilmT> page = new Page<>(1, nums);
            List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfo(films);
            filmVO.setFilmNum(films.size());
            filmVO.setFilmInfo(filmInfos);
        } else {
            Page<FilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfo(films);
            filmVO.setFilmNum(films.size());

            int totalCounts = filmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts / nums) + 1;


            filmVO.setFilmInfo(filmInfos);
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "3");
        Page<FilmT> page = null;
        switch (sortId) {
            case 1:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        if (sourceId != 99) {
            entityWrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats", catStr);
        }
        List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
        filmInfos = getFilmInfo(films);
        filmVO.setFilmNum(films.size());

        int totalCounts = filmTMapper.selectCount(entityWrapper);
        int totalPages = (totalCounts / nums) + 1;


        filmVO.setFilmInfo(filmInfos);
        filmVO.setTotalPage(totalPages);
        filmVO.setNowPage(nowPage);

        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<FilmT> page = new Page<>(1, 10, "film_box_office");
        List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(films);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        Page<FilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(films);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<FilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<FilmT> page = new Page<>(1, 10, "film_score");
        List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(films);
        return filmInfos;
    }

    @Override
    public List<CatVO> getCats() {
        List<CatVO> cats = new ArrayList<>();
        List<CatDictT> catDs = catDictTMapper.selectList(null);
        for (CatDictT catD : catDs) {
            CatVO catVO = new CatVO();
            catVO.setCatName(catD.getShowName());
            catVO.setCatId(catD.getUuid() + "");
            cats.add(catVO);
        }
        return cats;
    }

    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sources = new ArrayList<>();
        List<SourceDictT> sourceDictTS = sourceDictTMapper.selectList(null);
        for (SourceDictT sourceDictT : sourceDictTS) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceName(sourceDictT.getShowName());
            sourceVO.setSourceId(sourceDictT.getUuid() + "");
            sources.add(sourceVO);
        }
        return sources;
    }

    @Override
    public List<YearVO> getYears() {
        List<YearVO> years = new ArrayList<>();
        List<YearDictT> yearDictTS = yearDictTMapper.selectList(null);
        for (YearDictT yearDictT : yearDictTS) {
            YearVO yearVO = new YearVO();
            yearVO.setYearName(yearDictT.getShowName());
            yearVO.setYearId(yearDictT.getUuid() + "");
            years.add(yearVO);
        }
        return years;
    }

    @Override
    public FilmDetailVO getFilmDetail(Integer searchType, String searchParam) {
        FilmDetailVO filmDetailVO = null;
        if (searchType == 1) {
            filmDetailVO = filmTMapper.getFilmDetailByName("%" + searchParam + "%");
        } else {
            filmDetailVO = filmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;

    }

    private FilmInfoT getFilmInfo(String filmId) {
        FilmInfoT filmInfoT = new FilmInfoT();
        filmInfoT.setFilmId(filmId);
        FilmInfoT filmInfoT1 = filmInfoTMapper.selectOne(filmInfoT);
        return filmInfoT1;
    }

    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(filmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        String filmImgStr = filmInfoT.getFilmImgs();
        String[] filmImgs = filmImgStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    @Override
    public ActorVO getDectInfo(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        Integer directId = filmInfoT.getDirectorId();
        ActorT actorT = actorTMapper.selectById(directId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(actorT.getActorImg());
        actorVO.setDirectorName(actorT.getActorName());
        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = actorTMapper.getActors(filmId);

        return actors;
    }
}
