package uk.ac.bbsrc.tgac.miso.core.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DetailedSample extends Sample {

  public DetailedSample getParent();

  public void setParent(DetailedSample parent);

  public List<DetailedSample> getChildren();

  public void setChildren(List<DetailedSample> children);

  SampleClass getSampleClass();

  void setSampleClass(SampleClass sampleClass);

  Subproject getSubproject();

  void setSubproject(Subproject subproject);

  Boolean getArchived();

  void setArchived(Boolean archived);

  DetailedQcStatus getDetailedQcStatus();

  void setDetailedQcStatus(DetailedQcStatus detailedQcStatus);

  /**
   * @return the number of this Sample amongst Samples of the same SampleClass sharing the same parent
   */
  Integer getSiblingNumber();

  /**
   * Specifies the number of this Sample amongst Samples of the same SampleClass sharing the same parent
   * 
   * @param siblingNumber
   */
  void setSiblingNumber(Integer siblingNumber);

  /**
   * Gets the Group ID string of this sample analyte.
   * 
   * @return String groupId
   */
  String getGroupId();

  /**
   * Sets the Group ID string of this sample analyte.
   * 
   * @param Long groupId
   */
  void setGroupId(String groupId);

  /**
   * Gets the Group Description string of this sample analyte.
   * 
   * @return String groupDescription
   */
  String getGroupDescription();

  /**
   * Sets the Group Description string of this sample analyte.
   * 
   * @param String groupDescription
   */
  void setGroupDescription(String groupDescription);

  /**
   * True if the entity is not a physical sample, but one created to create the appearance of a complete hierarchy when partially processed
   * sample is received by the lab.
   */
  Boolean isSynthetic();

  void setSynthetic(Boolean synthetic);

  /**
   * True if the sample's alias does not pass alias validation but cannot be changed (usually for historical reasons). Setting this to true
   * means the sample will skip alias validation (and uniqueness validation, if enabled) during save.
   */
  boolean hasNonStandardAlias();

  void setNonStandardAlias(boolean nonStandardAlias);
  
  /**
   * @return the old LIMS' ID for this sample prior to being migrated to MISO
   */
  @Override
  Long getPreMigrationId();
  
  void setPreMigrationId(Long preMigrationId);

  String getDetailedQcStatusNote();

  void setDetailedQcStatusNote(String detailedQcStatusNote);

  /**
   * Transient field for storing the ID of the identity which will be at the root of the hierarchy for this Detailed Sample
   * 
   * @return Long identityId
   */
  Long getIdentityId();

  void setIdentityId(Long identityId);

  /**
   * Searches the sample hierarchy until a Group ID is found. Returns null if no sample has a Group ID.
   * 
   * @return Sample nearest sample with a non-empty group ID. May be the current sample.
   */
  Optional<DetailedSample> getEffectiveGroupIdSample();

  /**
   * Field for storing the date of sample creation. This is not the date that the sample was entered into MISO.
   * This field is not automatically generated on sample creation and must be specified by a user.
   * 
   * @return creationDate
   */
  Date getCreationDate();

  /**
   * Sets the date of sample creation to the specified date. This is not the date the sample was entered into MISO.
   * This field is not automatically generated on sample creation and must be specified by a user.
   * 
   * @param creationDate
   */
  void setCreationDate(Date creationDate);

}