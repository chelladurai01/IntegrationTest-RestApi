//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-146 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.05 at 12:04:43 AM IST 
//


package com.bestbuy.searchplatform.integrationtests.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Banner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="bannerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="bannerType" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="bannerPriority" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="bannerTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="contexts">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="searchProfileId" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                             &lt;element name="inherit" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                             &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="categoryPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="contextPathId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                   &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

/**
 * Class to declare the variables used to create Banners
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "banner"
})
@XmlRootElement(name = "Banners")
public class Banners {

    @XmlElement(name = "Banner", required = true)
    protected List<Banner> banner;

    /**
     * Gets the value of the banner property.
     * 
     * @return
     *     possible object is
     *     {@link Banners.Banner }
     *     
     */
    public List<Banner> getBanner() {
        return banner;
    }

    /**
     * Sets the value of the banner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Banners.Banner }
     *     
     */
    public void setBanner(List<Banner> value) {
        this.banner = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="bannerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="bannerType" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="bannerPriority" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="bannerTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="contexts">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="searchProfileId" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *                   &lt;element name="inherit" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *                   &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="categoryPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="contextPathId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bannerName",
        "bannerType",
        "bannerPriority",
        "bannerTemplate",
        "contexts",
        "documentId",
        "startDate",
        "endDate"
    })
    public static class Banner {

        @XmlElement(required = true)
        protected String bannerName;
        protected Long bannerType;
        protected Long bannerPriority;
        @XmlElement(required = true)
        protected String bannerTemplate;
        @XmlElement(required = true)
        protected List<Banners.Banner.Contexts> contexts;
        protected Long documentId;
        @XmlElement(required = true)
        protected String startDate;
        @XmlElement(required = true)
        protected String endDate;

        /**
         * Gets the value of the bannerName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBannerName() {
            return bannerName;
        }

        /**
         * Sets the value of the bannerName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBannerName(String value) {
            this.bannerName = value;
        }

        /**
         * Gets the value of the bannerType property.
         * 
         */
        public Long getBannerType() {
            return bannerType;
        }

        /**
         * Sets the value of the bannerType property.
         * 
         */
        public void setBannerType(Long value) {
            this.bannerType = value;
        }

        /**
         * Gets the value of the bannerPriority property.
         * 
         */
        public Long getBannerPriority() {
            return bannerPriority;
        }

        /**
         * Sets the value of the bannerPriority property.
         * 
         */
        public void setBannerPriority(Long value) {
            this.bannerPriority = value;
        }

        /**
         * Gets the value of the bannerTemplate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBannerTemplate() {
            return bannerTemplate;
        }

        /**
         * Sets the value of the bannerTemplate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBannerTemplate(String value) {
            this.bannerTemplate = value;
        }

        /**
         * Gets the value of the contexts property.
         * 
         * @return
         *     possible object is
         *     {@link Banners.Banner.Contexts }
         *     
         */
        public List<Contexts> getContexts() {
            return contexts;
        }

        /**
         * Sets the value of the contexts property.
         * 
         * @param value
         *     allowed object is
         *     {@link Banners.Banner.Contexts }
         *     
         */
        public void setContexts(List<Contexts> value) {
            this.contexts = value;
        }

        /**
         * Gets the value of the documentId property.
         * 
         */
        public long getDocumentId() {
            return documentId;
        }

        /**
         * Sets the value of the documentId property.
         * 
         */
        public void setDocumentId(long value) {
            this.documentId = value;
        }

        /**
         * Gets the value of the startDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStartDate() {
            return startDate;
        }

        /**
         * Sets the value of the startDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStartDate(String value) {
            this.startDate = value;
        }

        /**
         * Gets the value of the endDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEndDate() {
            return endDate;
        }

        /**
         * Sets the value of the endDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEndDate(String value) {
            this.endDate = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="searchProfileId" type="{http://www.w3.org/2001/XMLSchema}byte"/>
         *         &lt;element name="inherit" type="{http://www.w3.org/2001/XMLSchema}byte"/>
         *         &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="categoryPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="contextPathId" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "searchProfileId",
            "inherit",
            "keywords",
            "categoryPath",
            "contextPathId"
        })
        public static class Contexts {

            protected Long searchProfileId;
            protected Long inherit;
            @XmlElement(required = true)
            protected String keywords;
            @XmlElement(required = true)
            protected String categoryPath;
            @XmlElement(required = true)
            protected String contextPathId;

            /**
             * Gets the value of the searchProfileId property.
             * 
             */
            public Long getSearchProfileId() {
                return searchProfileId;
            }

            /**
             * Sets the value of the searchProfileId property.
             * 
             */
            public void setSearchProfileId(Long value) {
                this.searchProfileId = value;
            }

            /**
             * Gets the value of the inherit property.
             * 
             */
            public Long getInherit() {
                return inherit;
            }

            /**
             * Sets the value of the inherit property.
             * 
             */
            public void setInherit(Long value) {
                this.inherit = value;
            }

            /**
             * Gets the value of the keywords property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKeywords() {
                return keywords;
            }

            /**
             * Sets the value of the keywords property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKeywords(String value) {
                this.keywords = value;
            }

            /**
             * Gets the value of the categoryPath property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCategoryPath() {
                return categoryPath;
            }

            /**
             * Sets the value of the categoryPath property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCategoryPath(String value) {
                this.categoryPath = value;
            }

            /**
             * Gets the value of the contextPathId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContextPathId() {
                return contextPathId;
            }

            /**
             * Sets the value of the contextPathId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContextPathId(String value) {
                this.contextPathId = value;
            }

        }

    }

}