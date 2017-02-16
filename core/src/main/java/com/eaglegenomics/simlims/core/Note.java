package com.eaglegenomics.simlims.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import uk.ac.bbsrc.tgac.miso.core.data.Kit;
import uk.ac.bbsrc.tgac.miso.core.data.KitImpl;
import uk.ac.bbsrc.tgac.miso.core.data.Library;
import uk.ac.bbsrc.tgac.miso.core.data.Pool;
import uk.ac.bbsrc.tgac.miso.core.data.Run;
import uk.ac.bbsrc.tgac.miso.core.data.Sample;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.PoolImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.ProjectOverview;
import uk.ac.bbsrc.tgac.miso.core.data.impl.RunImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.UserImpl;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Abstract implementation of notes that provides all basic functionality.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
@Entity
@Table(name = "Note")
public class Note implements Serializable, Comparable<Note> {

  private static final long serialVersionUID = 1L;

  /**
   * Use this ID to indicate that a note has not yet been saved, and therefore
   * does not yet have a unique ID.
   */
  public static final Long UNSAVED_ID = null;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long noteId = Note.UNSAVED_ID;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date creationDate = new Date();

  @Column(nullable = false)
  private String text = "";

  @ManyToOne(targetEntity = UserImpl.class)
  @JoinColumn(name = "owner_userId", nullable = false)
  private User owner = null;

  @Column(nullable = false)
  private boolean internalOnly = false;

  @ManyToMany(targetEntity = PoolImpl.class)
  @JoinTable(name = "Pool_Note", inverseJoinColumns = {
      @JoinColumn(name = "pool_poolId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<Pool> pools = new HashSet<>();

  @ManyToMany(targetEntity = RunImpl.class)
  @JoinTable(name = "Run_Note", inverseJoinColumns = {
      @JoinColumn(name = "run_runId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<Run> runs = new HashSet<>();

  @ManyToMany(targetEntity = KitImpl.class)
  @JoinTable(name = "Kit_Note", inverseJoinColumns = {
      @JoinColumn(name = "kit_kitId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<Kit> kits = new HashSet<>();

  @ManyToMany(targetEntity = SampleImpl.class)
  @JoinTable(name = "Sample_Note", inverseJoinColumns = {
      @JoinColumn(name = "sample_sampleId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<Sample> samples = new HashSet<>();

  @ManyToMany(targetEntity = LibraryImpl.class)
  @JoinTable(name = "Library_Note", inverseJoinColumns = {
      @JoinColumn(name = "library_libraryId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<Library> libraries = new HashSet<>();

  @ManyToMany(targetEntity = ProjectOverview.class)
  @JoinTable(name = "ProjectOverview_Note", inverseJoinColumns = {
      @JoinColumn(name = "overview_overviewId") }, joinColumns = {
          @JoinColumn(name = "notes_noteId") })
  @JsonBackReference
  private final Collection<ProjectOverview> overviews = new HashSet<>();

  public Note() {
  }

  public Note(User user) {
    setOwner(user);
  }

  /**
   * Internal use only.
   */
  public Long getNoteId() {
    return noteId;
  }

  public void setNoteId(Long noteId) {
    this.noteId = noteId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public String getText() {
    return text;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getOwner() {
    return owner;
  }

  public boolean isInternalOnly() {
    return internalOnly;
  }

  public void setInternalOnly(boolean internalOnly) {
    this.internalOnly = internalOnly;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  /**
   * Notes are equivalent based on creation date, owner and text.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (!(obj instanceof Note))
      return false;
    Note them = (Note) obj;
    return this.getCreationDate().equals(them.getCreationDate())
        && this.getOwner().equals(them.getOwner())
        && this.getText().equals(them.getText());
  }

  @Override
  public int hashCode() {
    if (getNoteId() != null && !getNoteId().equals(Note.UNSAVED_ID)) {
      return getNoteId().hashCode();
    }
    else {
      int hashcode = 1;
      if (getCreationDate() != null) hashcode = 37 * hashcode + getCreationDate().hashCode();
      if (getOwner() != null) hashcode = 37 * hashcode + getOwner().hashCode();
      if (getText() != null) hashcode = 37 * hashcode + getText().hashCode();
      return hashcode;
    }
  }

  /**
   * Format is "Date: Owner: Text".
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(getCreationDate());
    sb.append(" : ");
    sb.append(getOwner());
    sb.append(" : ");
    sb.append(getText());
    return sb.toString();
  }

  @Override
  public int compareTo(Note o) {
    if (getNoteId() != null && o.getNoteId() != null) {
      if (getNoteId() < o.getNoteId()) return -1;
      if (getNoteId() > o.getNoteId()) return 1;
    }
    else if (getText() != null && o.getText() != null) {
      return getText().compareTo(o.getText());
    }
    return 0;
  }
}
