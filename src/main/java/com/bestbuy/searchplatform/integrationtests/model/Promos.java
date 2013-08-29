//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-146 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.04 at 06:58:17 PM GMT+05:30 
//


package com.bestbuy.searchplatform.integrationtests.model;

import java.util.ArrayList;
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
 *         &lt;element name="promo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="promoName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="skuIds" type="{http://www.w3.org/2001/XMLSchema}List<String>"/>
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
 * Class to declare the variables used to create Promos
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "promo"
})
@XmlRootElement(name = "promos")
public class Promos {

    protected List<Promos.Promo> promo;

    /**
     * Gets the value of the promo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the promo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPromo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Promos.Promo }
     * 
     * 
     */
    public List<Promos.Promo> getPromo() {
        if (promo == null) {
            promo = new ArrayList<Promos.Promo>();
        }
        return this.promo;
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
     *         &lt;element name="promoName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="skuIds" type="{http://www.w3.org/2001/XMLSchema}short"/>
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
        "promoName",
        "skuIds",
        "startDate",
        "endDate"
    })
    public static class Promo {

        @XmlElement(required = true)
        protected String promoName;
        protected List<String> skuIds;
        @XmlElement(required = true)
        protected String startDate;
        @XmlElement(required = true)
        protected String endDate;

        /**
         * Gets the value of the promoName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPromoName() {
            return promoName;
        }

        /**
         * Sets the value of the promoName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPromoName(String value) {
            this.promoName = value;
        }

        /**
         * Gets the value of the skuIds property.
         * 
         */
        public List<String> getSkuIds() {
            return skuIds;
        }

        /**
         * Sets the value of the skuIds property.
         * 
         */
        public void setSkuIds(List<String> value) {
            this.skuIds = value;
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

    }

}