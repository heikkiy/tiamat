//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 10:37:32 AM CET 
//


package no.rutebanken.tiamat.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.netex.org.uk/netex}AccessSpace_VersionStructure">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}EntityInVersionGroup" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}DataManagedObjectGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}GroupOfEntitiesGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}GroupOfPointsGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}ZoneGroup"/>
 *         &lt;/sequence>
 *         &lt;group ref="{http://www.netex.org.uk/netex}PlaceGroup"/>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}AddressablePlaceGroup"/>
 *         &lt;/sequence>
 *         &lt;group ref="{http://www.netex.org.uk/netex}SiteElementGroup"/>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}SiteComponentGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}StopPlaceComponentPropertyGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}StopPlaceSpaceGroup"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://www.netex.org.uk/netex}AccessSpaceGroup"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.netex.org.uk/netex}AccessSpaceIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@Entity
public class AccessSpace
    extends AccessSpace_VersionStructure
{


}
