curve_editor -input spline01_bezier.txt -gui -curve_tessellation 30
curve_editor -input spline02_bspline.txt -gui -curve_tessellation 30curve_editor -input spline01_bezier.txt -output_bezier output01_bezier.txt
curve_editor -input spline01_bezier.txt -output_bspline output01_bspline.txt
curve_editor -input spline02_bspline.txt -output_bezier output02_bezier.txt
curve_editor -input spline02_bspline.txt -output_bspline output02_bspline.txt
curve_editor -input output01_bezier.txt -gui -curve_tessellation 30
curve_editor -input output01_bspline.txt -gui -curve_tessellation 30
curve_editor -input output02_bezier.txt -gui -curve_tessellation 30
curve_editor -input output02_bspline.txt -gui -curve_tessellation 30curve_editor -input spline03_bezier.txt -gui -curve_tessellation 30
curve_editor -input spline04_bspline.txt -gui -curve_tessellation 30
curve_editor -input spline05_bspline_dups.txt -gui -curve_tessellation 30curve_editor -input spline06_torus.txt -curve_tessellation 4 -gui
curve_editor -input spline06_torus.txt -curve_tessellation 4 -revolution_tessellation 10 -output torus_low.obj
curve_editor -input spline06_torus.txt -curve_tessellation 30 -revolution_tessellation 60 -output torus_high.obj
raytracer -input scene06_torus_low.txt -gui -size 300 300
raytracer -input scene06_torus_high.txt -gui -size 300 300curve_editor -input spline07_vase.txt -curve_tessellation 4 -output_bspline output07_edit.txt -gui
curve_editor -input output07_edit.txt -curve_tessellation 4 -revolution_tessellation 10 -output vase_low.obj
curve_editor -input output07_edit.txt -curve_tessellation 10 -revolution_tessellation 60 -output vase_high.obj
raytracer -input scene07_vase_low.txt -gui -size 300 300
raytracer -input scene07_vase_high.txt -gui -size 300 300curve_editor -input spline08_bezier_patch.txt -gui
curve_editor -input spline08_bezier_patch.txt -patch_tessellation 4 -output patch_low.obj
curve_editor -input spline08_bezier_patch.txt -patch_tessellation 10 -output patch_med.obj
curve_editor -input spline08_bezier_patch.txt -patch_tessellation 40 -output patch_high.obj
raytracer -input scene08_bezier_patch_low.txt -gui -size 300 300
raytracer -input scene08_bezier_patch_med.txt -gui -size 300 300
raytracer -input scene08_bezier_patch_high.txt -gui -size 300 300curve_editor -input spline09_teapot.txt -curve_tessellation 4 -gui
curve_editor -input spline09_teapot.txt -patch_tessellation 4 -curve_tessellation 4 -revolution_tessellation 10 -output teapot_low.obj
curve_editor -input spline09_teapot.txt -patch_tessellation 30 -curve_tessellation 30 -revolution_tessellation 100 -output teapot_high.obj
raytracer -input scene09_teapot_low.txt -gui -size 300 300
raytracer -input scene09_teapot_high.txt -gui -size 300 300