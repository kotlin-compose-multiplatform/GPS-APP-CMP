package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Prp(
    val access_templates: String? = null,
    val autocomplete: String? = null,
    val dst: String? = null,
    val forceAddedDashboardTabOnce: String? = null,
    val fpnl: String? = null,
    val geodata_source: String? = null,
    val hbacit: String? = null,
    val hpnl: String? = null,
    val language: String? = null,
    val m_ml: String? = null,
    val m_mm: String? = null,
    val m_mm2: String? = null,
    val m_monu: String? = null,
    val m_ms: String? = null,
    val minimap_zoom_level: String? = null,
    val mongr: String? = null,
    val mont: String? = null,
    val monu: String? = null,
    val monuv: String? = null,
    val mtg: String? = null,
    val mtve: String? = null,
    val mu_battery: String? = null,
    val mu_dev_cfg: String? = null,
    val mu_fast_report: String? = null,
    val mu_fast_report_tmpl: String? = null,
    val mu_fast_track_ival: String? = null,
    val mu_sens: String? = null,
    val mu_tbl_cols_sizes: String? = null,
    val mu_tbl_sort: String? = null,
    val mu_tracks: String? = null,
    val radd: String? = null,
    val show_log: String? = null,
    val tz: String? = null,
    val umap: String? = null,
    val us_addr_fmt: String? = null,
    val used_hw: String? = null,
    val znsvlist: String? = null
)