package com.merchantanalysis.service.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.common.exception.BusinessException;
import com.merchantanalysis.dto.request.MerchantQueryRequest;
import com.merchantanalysis.dto.request.MerchantRequest;
import com.merchantanalysis.dto.response.*;
import com.merchantanalysis.entity.*;
import com.merchantanalysis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final MerchantSocialAccountMapper socialAccountMapper;
    private final MerchantProductMapper productMapper;
    private final MerchantFinancialMapper financialMapper;
    private final MerchantOperatingDataMapper operatingDataMapper;
    private final MerchantReviewMapper reviewMapper;
    private final IndustryCategoryMapper industryCategoryMapper;

    @Override
    public IPage<MerchantDetailVO> pageList(MerchantQueryRequest request) {
        Page<Merchant> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<Merchant> result = merchantMapper.selectPageList(page, request);
        return result.convert(this::convertToVO);
    }

    @Override
    public MerchantDetailVO getDetail(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        return buildDetailVO(merchant);
    }

    @Override
    @Transactional
    public Long add(MerchantRequest request) {
        Merchant merchant = convertToEntity(request);
        merchantMapper.insert(merchant);
        return merchant.getId();
    }

    @Override
    @Transactional
    public void update(MerchantRequest request) {
        if (request.getId() == null) {
            throw new BusinessException("商家ID不能为空");
        }
        Merchant existing = merchantMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException("商家不存在");
        }
        Merchant merchant = convertToEntity(request);
        merchant.setId(request.getId());
        merchantMapper.updateById(merchant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        merchantMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        merchantMapper.deleteBatchIds(ids);
    }

    @Override
    public List<MerchantDetailVO> listAll() {
        List<Merchant> list = merchantMapper.selectList(new LambdaQueryWrapper<>());
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public void updateDataStatus(Long id, Integer dataStatus) {
        Merchant merchant = new Merchant();
        merchant.setId(id);
        merchant.setDataStatus(dataStatus);
        merchantMapper.updateById(merchant);
    }

    private MerchantDetailVO buildDetailVO(Merchant merchant) {
        MerchantDetailVO vo = convertToVO(merchant);
        vo.setSocialAccounts(socialAccountMapper.selectByMerchantId(merchant.getId())
                .stream().map(this::convertSocialAccount).collect(Collectors.toList()));
        vo.setProducts(productMapper.selectByMerchantId(merchant.getId())
                .stream().map(this::convertProduct).collect(Collectors.toList()));
        vo.setFinancials(financialMapper.selectByMerchantId(merchant.getId())
                .stream().map(this::convertFinancial).collect(Collectors.toList()));
        vo.setOperatingDataList(operatingDataMapper.selectByMerchantId(merchant.getId())
                .stream().map(this::convertOperatingData).collect(Collectors.toList()));
        vo.setReviews(reviewMapper.selectByMerchantId(merchant.getId())
                .stream().map(this::convertReview).collect(Collectors.toList()));
        return vo;
    }

    private MerchantDetailVO convertToVO(Merchant merchant) {
        MerchantDetailVO vo = new MerchantDetailVO();
        vo.setId(merchant.getId());
        vo.setName(merchant.getName());
        vo.setWebsite(merchant.getWebsite());
        vo.setIsListed(merchant.getIsListed());
        vo.setListingExchange(merchant.getListingExchange());
        vo.setStockCode(merchant.getStockCode());
        vo.setIndustryId(merchant.getIndustryId());
        vo.setCompanyScale(merchant.getCompanyScale());
        vo.setRegisteredCapital(merchant.getRegisteredCapital());
        vo.setEstablishedDate(merchant.getEstablishedDate());
        vo.setBusinessDescription(merchant.getBusinessDescription());
        vo.setAdvantages(merchant.getAdvantages());
        vo.setProductsInfo(merchant.getProductsInfo());
        vo.setRevenue(merchant.getRevenue());
        vo.setEmployeeCount(merchant.getEmployeeCount());
        vo.setHeadquarters(merchant.getHeadquarters());
        vo.setSourceUrl(merchant.getSourceUrl());
        vo.setDataStatus(merchant.getDataStatus());
        vo.setLastFetchTime(merchant.getLastFetchTime());
        vo.setCreateTime(merchant.getCreateTime());
        vo.setUpdateTime(merchant.getUpdateTime());
        if (merchant.getIndustryId() != null) {
            IndustryCategory cat = industryCategoryMapper.selectById(merchant.getIndustryId());
            if (cat != null) {
                vo.setIndustryName(cat.getName());
            }
        }
        return vo;
    }

    private Merchant convertToEntity(MerchantRequest request) {
        Merchant merchant = new Merchant();
        merchant.setName(request.getName());
        merchant.setWebsite(request.getWebsite());
        merchant.setIsListed(request.getIsListed());
        merchant.setListingExchange(request.getListingExchange());
        merchant.setStockCode(request.getStockCode());
        merchant.setIndustryId(request.getIndustryId());
        merchant.setCompanyScale(request.getCompanyScale());
        merchant.setRegisteredCapital(request.getRegisteredCapital());
        merchant.setEstablishedDate(request.getEstablishedDate());
        merchant.setBusinessDescription(request.getBusinessDescription());
        merchant.setAdvantages(request.getAdvantages());
        merchant.setProductsInfo(request.getProductsInfo());
        merchant.setRevenue(request.getRevenue());
        merchant.setEmployeeCount(request.getEmployeeCount());
        merchant.setHeadquarters(request.getHeadquarters());
        merchant.setSourceUrl(request.getSourceUrl());
        merchant.setDataStatus(0);
        return merchant;
    }

    private SocialAccountVO convertSocialAccount(MerchantSocialAccount a) {
        SocialAccountVO vo = new SocialAccountVO();
        vo.setId(a.getId());
        vo.setMerchantId(a.getMerchantId());
        vo.setPlatform(a.getPlatform());
        vo.setAccountName(a.getAccountName());
        vo.setAccountId(a.getAccountId());
        vo.setAccountUrl(a.getAccountUrl());
        vo.setFollowersCount(a.getFollowersCount());
        vo.setFollowingCount(a.getFollowingCount());
        vo.setPostsCount(a.getPostsCount());
        vo.setVerificationStatus(a.getVerificationStatus());
        vo.setLastFetchTime(a.getLastFetchTime());
        return vo;
    }

    private ProductVO convertProduct(MerchantProduct p) {
        ProductVO vo = new ProductVO();
        vo.setId(p.getId());
        vo.setMerchantId(p.getMerchantId());
        vo.setProductName(p.getProductName());
        vo.setCategory(p.getCategory());
        vo.setPrice(p.getPrice());
        vo.setSalesCount(p.getSalesCount());
        vo.setRating(p.getRating());
        vo.setProductUrl(p.getProductUrl());
        vo.setDescription(p.getDescription());
        vo.setTags(p.getTags());
        vo.setSource(p.getSource());
        vo.setUpdateTime(p.getUpdateTime());
        return vo;
    }

    private FinancialVO convertFinancial(MerchantFinancial f) {
        FinancialVO vo = new FinancialVO();
        vo.setId(f.getId());
        vo.setMerchantId(f.getMerchantId());
        vo.setReportYear(f.getReportYear());
        vo.setReportPeriod(f.getReportPeriod());
        vo.setReportDate(f.getReportDate());
        vo.setRevenue(f.getRevenue());
        vo.setNetProfit(f.getNetProfit());
        vo.setGrossMargin(f.getGrossMargin());
        vo.setOperatingMargin(f.getOperatingMargin());
        vo.setTotalAssets(f.getTotalAssets());
        vo.setTotalLiabilities(f.getTotalLiabilities());
        vo.setRoe(f.getRoe());
        vo.setEps(f.getEps());
        vo.setSource(f.getSource());
        return vo;
    }

    private OperatingDataVO convertOperatingData(MerchantOperatingData d) {
        OperatingDataVO vo = new OperatingDataVO();
        vo.setId(d.getId());
        vo.setMerchantId(d.getMerchantId());
        vo.setPlatform(d.getPlatform());
        vo.setDataDate(d.getDataDate());
        vo.setGmv(d.getGmv());
        vo.setSalesAmount(d.getSalesAmount());
        vo.setOrdersCount(d.getOrdersCount());
        vo.setAvgOrderPrice(d.getAvgOrderPrice());
        vo.setConversionRate(d.getConversionRate());
        vo.setTrafficCount(d.getTrafficCount());
        vo.setFansGrowth(d.getFansGrowth());
        vo.setEngagementRate(d.getEngagementRate());
        vo.setLiveSales(d.getLiveSales());
        vo.setAdInvestment(d.getAdInvestment());
        vo.setRoi(d.getRoi());
        vo.setHotProductsRank(d.getHotProductsRank());
        vo.setCategoryRank(d.getCategoryRank());
        vo.setMarketShare(d.getMarketShare());
        return vo;
    }

    private ReviewVO convertReview(MerchantReview r) {
        ReviewVO vo = new ReviewVO();
        vo.setId(r.getId());
        vo.setMerchantId(r.getMerchantId());
        vo.setProductId(r.getProductId());
        vo.setPlatform(r.getPlatform());
        vo.setReviewerNickname(r.getReviewerNickname());
        vo.setRating(r.getRating());
        vo.setContent(r.getContent());
        vo.setLikeCount(r.getLikeCount());
        vo.setReplyCount(r.getReplyCount());
        vo.setMediaUrls(r.getMediaUrls());
        vo.setSentiment(r.getSentiment());
        vo.setKeywords(r.getKeywords());
        vo.setReviewDate(r.getReviewDate());
        vo.setSourceUrl(r.getSourceUrl());
        return vo;
    }
}
